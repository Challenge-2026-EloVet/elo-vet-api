package com.br.elovetapi.care.service;

import com.br.elovetapi.care.dtos.CarePlanRequestDTO;
import com.br.elovetapi.care.dtos.CarePlanResponseDTO;
import com.br.elovetapi.care.dtos.MarkItemRequestDTO;
import com.br.elovetapi.care.mapper.CarePlanMapper;
import com.br.elovetapi.care.model.CarePlan;
import com.br.elovetapi.care.model.CarePlanItem;
import com.br.elovetapi.care.model.Notification;
import com.br.elovetapi.care.repository.CarePlanItemRepository;
import com.br.elovetapi.care.repository.CarePlanRepository;
import com.br.elovetapi.care.repository.NotificationRepository;
import com.br.elovetapi.user.repository.UserRepository;
import com.br.elovetapi.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final CarePlanItemRepository carePlanItemRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public CarePlanService(CarePlanRepository carePlanRepository, CarePlanItemRepository carePlanItemRepository, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.carePlanRepository = carePlanRepository;
        this.carePlanItemRepository = carePlanItemRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CarePlanResponseDTO createCarePlan(Long veterinaryId, CarePlanRequestDTO dto){
        if(dto.petId() == null) throw new IllegalArgumentException("petId is required");
        if(dto.petOwnerId() == null) throw new IllegalArgumentException("petOwnerId is required");
        if(dto.items() == null || dto.items().isEmpty()) throw new IllegalArgumentException("care plan must have at least one item");

        Optional<User> ownerOpt = userRepository.findById(dto.petOwnerId());
        if(ownerOpt.isEmpty()) throw new IllegalArgumentException("petOwnerId not found");

        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long creatorId = principal.getId();

        CarePlan cp = CarePlanMapper.toEntity(veterinaryId, dto, creatorId);
        CarePlan saved = carePlanRepository.save(cp);

        List<CarePlanItem> items = dto.items().stream().map(i -> CarePlanMapper.toItemEntity(saved.getId(), i)).collect(Collectors.toList());
        var savedItems = carePlanItemRepository.saveAll(items);

        Notification n = new Notification();
        n.setTargetUserId(dto.petOwnerId());
        n.setCarePlanId(saved.getId());
        n.setType("HANDOFF");
        n.setPayload("Care plan created with " + savedItems.size() + " items");
        n.setSentAt(LocalDateTime.now());
        notificationRepository.save(n);

        return CarePlanMapper.toDTO(saved, savedItems);
    }

    public List<CarePlanResponseDTO> listCarePlansForUser(Long userId){
        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getUserRole() != null && principal.getUserRole().name().equals("ADMIN");
        if(!isAdmin && !principal.getId().equals(userId)) throw new SecurityException("not authorized");

        List<CarePlan> plans = carePlanRepository.findByPetOwnerId(userId);
        return plans.stream().map(p -> {
            List<CarePlanItem> items = carePlanItemRepository.findByCarePlanId(p.getId());
            return CarePlanMapper.toDTO(p, items);
        }).collect(Collectors.toList());
    }

    @Transactional
    public CarePlanResponseDTO markItem(Long carePlanId, Long itemId, MarkItemRequestDTO dto){
        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<CarePlan> cpOpt = carePlanRepository.findById(carePlanId);
        if(cpOpt.isEmpty()) throw new IllegalArgumentException("care plan not found");
        CarePlan cp = cpOpt.get();
        boolean isAdmin = principal.getUserRole() != null && principal.getUserRole().name().equals("ADMIN");
        if(!isAdmin && !principal.getId().equals(cp.getPetOwnerId())) throw new SecurityException("not authorized");

        Optional<CarePlanItem> itemOpt = carePlanItemRepository.findById(itemId);
        if(itemOpt.isEmpty()) throw new IllegalArgumentException("item not found");
        CarePlanItem item = itemOpt.get();
        if(!item.getCarePlanId().equals(carePlanId)) throw new IllegalArgumentException("item does not belong to care plan");

        item.setStatus(dto.status());
        carePlanItemRepository.save(item);


        Notification audit = new Notification();
        audit.setTargetUserId(principal.getId());
        audit.setCarePlanId(carePlanId);
        audit.setType("AUDIT_MARK_ITEM");
        audit.setPayload("Item " + item.getId() + " marked " + dto.status() + ". Note: " + dto.note());
        audit.setSentAt(LocalDateTime.now());
        notificationRepository.save(audit);

        List<CarePlanItem> items = carePlanItemRepository.findByCarePlanId(carePlanId);
        return CarePlanMapper.toDTO(cp, items);
    }

    public int processReminders(){
        var today = java.time.LocalDate.now();
        List<CarePlanItem> items = carePlanItemRepository.findAll().stream().filter(i -> i.getStatus() != null && i.getStatus().equals("PENDING") && i.getDueDate() != null && i.getDueDate().isEqual(today)).collect(Collectors.toList());
        int count = 0;
        for(var item: items){
            Optional<CarePlan> cpOpt = carePlanRepository.findById(item.getCarePlanId());
            if(cpOpt.isEmpty()) continue;
            CarePlan cp = cpOpt.get();
            Notification n = new Notification();
            n.setTargetUserId(cp.getPetOwnerId());
            n.setCarePlanId(cp.getId());
            n.setType("REMINDER");
            n.setPayload("Reminder: " + item.getTitle());
            n.setSentAt(LocalDateTime.now());
            notificationRepository.save(n);

            Notification audit = new Notification();
            audit.setTargetUserId(cp.getPetOwnerId());
            audit.setCarePlanId(cp.getId());
            audit.setType("AUDIT_REMINDER");
            audit.setPayload("Reminder sent for item " + item.getId());
            audit.setSentAt(LocalDateTime.now());
            notificationRepository.save(audit);
            count++;
        }
        return count;
    }
}