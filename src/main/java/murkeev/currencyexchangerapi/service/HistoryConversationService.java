package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.HistoryConversationDto;
import murkeev.currencyexchangerapi.dto.HistoryUpdateDto;
import murkeev.currencyexchangerapi.dto.UserHistoryDto;
import murkeev.currencyexchangerapi.entity.HistoryConversation;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.exceptions.EntityManipulationException;
import murkeev.currencyexchangerapi.exceptions.EntityNotFoundException;
import murkeev.currencyexchangerapi.repository.HistoryConversationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class HistoryConversationService {
    private final HistoryConversationRepository historyConversationRepository;
    private final UserService userService;
    private final CacheService cacheService;
    private final ModelMapper modelMapper;


    public void saveConversionHistory(HistoryConversationDto historyConversationDto) {
        User user = userService.getCurrentUser();
        HistoryConversation historyConversation = modelMapper.map(historyConversationDto, HistoryConversation.class);
        historyConversation.setUser(user);
        historyConversationRepository.save(historyConversation);
    }

    public Page<UserHistoryDto> getUserConversionHistory(Long userId, Pageable pageable) {
        Page<HistoryConversation> historyConversationPage = historyConversationRepository.findAllConversionsByUserId(userId, pageable);
        if (historyConversationPage.isEmpty()) {
            throw new EntityNotFoundException("No history data!");
        }

        return historyConversationPage
                .map(historyConversation -> modelMapper.map(historyConversation, UserHistoryDto.class));
    }


    public Page<UserHistoryDto> orderBy(String value, Pageable pageable) {
        if (!List.of("date", "currencyValue").contains(value)) {
            throw new IllegalArgumentException("Invalid sort value: " + value);
        }

        Page<HistoryConversation> historyConversationPage = switch (value) {
            case "date" -> historyConversationRepository.orderByDate(pageable);
            case "currencyValue" -> historyConversationRepository.orderByCurrencyValue(pageable);
            default -> throw new IllegalArgumentException("Unexpected sort value: " + value);
        };

        if (historyConversationPage.isEmpty()) {
            throw new EntityNotFoundException("No history data!");
        }

        return historyConversationPage.map(historyConversation -> modelMapper.map(historyConversation, UserHistoryDto.class));
    }

    public Page<UserHistoryDto> getUserHistoryConversation(Pageable pageable) {
        User user = userService.getCurrentUser();

        Page<HistoryConversation> historyConversationPage = historyConversationRepository
                .getUserHistoryConversation(user.getId(), pageable);

        if (historyConversationPage.isEmpty()) {
            throw new EntityNotFoundException("No history data!");
        }
        return historyConversationPage.map(historyConversation -> modelMapper.map(historyConversation, UserHistoryDto.class));
    }

    public void removeHistory(Long id) {
        HistoryConversation historyConversation = historyConversationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("History not found"));
        String cacheKey = cacheService.createCacheKey("userHistoryConversation:", historyConversation.getUser().getId());
        cacheService.removeValue(cacheKey);

        try {
            historyConversationRepository.deleteById(historyConversation.getId());
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in update history");
        }
    }

    public void createHistory(UserHistoryDto userHistoryDto) {
        HistoryConversation saveHistory = modelMapper.map(userHistoryDto, HistoryConversation.class);
        try {
            historyConversationRepository.save(saveHistory);
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in update history");
        }
    }

    public void updateHistory(HistoryUpdateDto updateHistory) {
        HistoryConversation existingHistory = historyConversationRepository.findById(updateHistory.getId())
                .orElseThrow(() -> new EntityNotFoundException("History not found"));
        modelMapper.map(updateHistory, existingHistory);
        try {
            historyConversationRepository.save(existingHistory);
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in update history");
        }
    }

    public UserHistoryDto findById(Long id) {
        String cacheKey = cacheService.createCacheKey("userHistoryConversation:", id);
        UserHistoryDto cachedHistory = (UserHistoryDto) cacheService.getValue(cacheKey);

        if (cachedHistory != null) {
            return cachedHistory;
        }

        HistoryConversation historyConversation = historyConversationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("History not found"));
        UserHistoryDto userHistoryDto = modelMapper.map(historyConversation, UserHistoryDto.class);
        cacheService.saveValue(cacheKey, userHistoryDto, 3, TimeUnit.HOURS);
        return userHistoryDto;
    }

    public Page<UserHistoryDto> findByDate(LocalDate date, Pageable pageable) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        Page<HistoryConversation> historyConversations = historyConversationRepository
                .findAllByDateBetween(start, end, pageable);
        if (historyConversations.isEmpty()) {
            throw new EntityNotFoundException("No history data!");
        }
        return historyConversations.map(
                conversation -> modelMapper.map(conversation, UserHistoryDto.class));
    }
}
