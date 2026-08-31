package com.br.elovetapi.care.service;

import com.br.elovetapi.care.dtos.CarePlanRequestDTO;
import com.br.elovetapi.care.dtos.CarePlanResponseDTO;
import com.br.elovetapi.care.dtos.CarePlanItemDTO;
import com.br.elovetapi.care.dtos.MarkItemRequestDTO;
import com.br.elovetapi.care.exceptions.CarePlanNotFoundException;
import com.br.elovetapi.care.exceptions.ItemDoesNotBelongCarePlanException;
import com.br.elovetapi.care.exceptions.ItemNotFoundException;
import com.br.elovetapi.care.mapper.CarePlanMapper;
import com.br.elovetapi.care.model.CarePlan;
import com.br.elovetapi.care.model.CarePlanItem;
import com.br.elovetapi.care.repository.CarePlanItemRepository;
import com.br.elovetapi.care.repository.CarePlanRepository;
import com.br.elovetapi.pet.exceptions.PetNotFoundException;
import com.br.elovetapi.user.repository.UserRepository;
import com.br.elovetapi.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.br.elovetapi.care.exceptions.CarePlanValidationException;

@Service
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final CarePlanItemRepository carePlanItemRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public CarePlanService(CarePlanRepository carePlanRepository, CarePlanItemRepository carePlanItemRepository, NotificationService notificationService, UserRepository userRepository) {
        this.carePlanRepository = carePlanRepository;
        this.carePlanItemRepository = carePlanItemRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Transactional
    public CarePlanResponseDTO createCarePlan(Long veterinaryId, CarePlanRequestDTO dto){
        validateCreateDto(dto);

        userRepository.findById(dto.petOwnerId()).orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + dto.petId()));

        User creator = getAuthenticatedUser();

        CarePlan cp = CarePlanMapper.toEntity(veterinaryId, dto, creator.getId());
        CarePlan saved = carePlanRepository.save(cp);

        List<CarePlanItem> items = saveItems(saved.getId(), dto.items());

        notificationService.createNotification(dto.petOwnerId(), saved.getId(), "HANDOFF", "Care plan created with " + items.size() + " items");

        return CarePlanMapper.toDTO(saved, items);
    }

    public List<CarePlanResponseDTO> listCarePlansForUser(Long userId){
        User principal = getAuthenticatedUser();
        assertOwnerOrAdmin(userId, principal);

        List<CarePlan> plans = carePlanRepository.findByPetOwnerId(userId);
        return plans.stream()
                .map(p -> CarePlanMapper.toDTO(p, carePlanItemRepository.findByCarePlanId(p.getId())))
                .toList();
    }

    @Transactional
    public CarePlanResponseDTO markItem(Long carePlanId, Long itemId, MarkItemRequestDTO dto){
        User principal = getAuthenticatedUser();

        CarePlan cp = carePlanRepository.findById(carePlanId).orElseThrow(() -> new CarePlanNotFoundException("Care plan not found"));
        assertOwnerOrAdmin(cp.getPetOwnerId(), principal);

        CarePlanItem item = carePlanItemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        if(!Objects.equals(item.getCarePlanId(), carePlanId)) throw new ItemDoesNotBelongCarePlanException("Item does not belong to care plan");

        item.setStatus(dto.status());
        carePlanItemRepository.save(item);

        notificationService.createNotification(principal.getId(), carePlanId, "AUDIT_MARK_ITEM", "Item " + item.getId() + " marked " + dto.status() + ". Note: " + dto.note());

        return CarePlanMapper.toDTO(cp, carePlanItemRepository.findByCarePlanId(carePlanId));
    }

    public int processReminders() {
        LocalDate today = LocalDate.now();
        List<CarePlanItem> dueItems = carePlanItemRepository.findAll().stream()
                .filter(i -> "PENDING".equals(i.getStatus()) && i.getDueDate() != null && i.getDueDate().isEqual(today))
                .toList();

        dueItems.forEach(item ->
                carePlanRepository.findById(item.getCarePlanId()).ifPresent(cp -> {
                    notificationService.createNotification(cp.getPetOwnerId(), cp.getId(), "REMINDER", "Reminder: " + item.getTitle());
                    notificationService.createNotification(cp.getPetOwnerId(), cp.getId(), "AUDIT_REMINDER", "Reminder sent for item " + item.getId());
                })
        );

        return dueItems.size();
    }

    private void validateCreateDto(CarePlanRequestDTO dto) {
        require(dto != null, "Care plan request is required");
        require(dto.petId() != null, "PetId is required");
        require(dto.petOwnerId() != null, "PetOwnerId is required");
        require(dto.items() != null && !dto.items().isEmpty(), "Care plan must have at least one item");
    }

    private void require(boolean expression, String message) {
        if (!expression) throw new CarePlanValidationException(message);
    }

    private User getAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) throw new SecurityException("Not authenticated");
        return (User) auth.getPrincipal();
    }

    private boolean isAdmin(User user) {
        return user.getUserRole() != null && user.getUserRole().name().equals("ADMIN");
    }

    private void assertOwnerOrAdmin(Long ownerId, User principal) {
        if (!isAdmin(principal) && !Objects.equals(principal.getId(), ownerId)) throw new SecurityException("Not authorized");
    }

    private List<CarePlanItem> saveItems(Long carePlanId, List<CarePlanItemDTO> itemsDto) {
        List<CarePlanItem> items = itemsDto.stream().map(i -> CarePlanMapper.toItemEntity(carePlanId, i)).toList();
        return carePlanItemRepository.saveAll(items);
    }
}