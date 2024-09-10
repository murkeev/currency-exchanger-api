package murkeev.currencyexchangerapi.controllers;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.UserHistoryDto;
import murkeev.currencyexchangerapi.service.HistoryConversationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final HistoryConversationService historyConversationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public List<UserHistoryDto> historyConversations(@PathVariable Long id) {
        return historyConversationService.getConversionHistory(id);
    }
}
