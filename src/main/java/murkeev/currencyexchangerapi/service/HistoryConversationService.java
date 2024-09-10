package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.HistoryConversationDto;
import murkeev.currencyexchangerapi.dto.UserHistoryDto;
import murkeev.currencyexchangerapi.entity.HistoryConversation;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.repository.HistoryConversationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryConversationService {
    private final HistoryConversationRepository historyConversationRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;


    public void saveConversionHistory(HistoryConversationDto historyConversationDto) {
        User user = userService.getCurrentUser();

        HistoryConversation historyConversation = modelMapper.map(historyConversationDto, HistoryConversation.class);
        historyConversation.setUser(user);
        historyConversationRepository.save(historyConversation);
    }

    public List<UserHistoryDto> getConversionHistory(Long userId) {
        List<HistoryConversation> historyList = historyConversationRepository.findByUserId(userId);
        return historyList.stream()
                .map(conversation -> modelMapper.map(conversation, UserHistoryDto.class))
                .collect(Collectors.toList());
    }
}
