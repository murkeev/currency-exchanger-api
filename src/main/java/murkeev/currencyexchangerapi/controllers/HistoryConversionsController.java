package murkeev.currencyexchangerapi.controllers;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.HistoryUpdateDto;
import murkeev.currencyexchangerapi.dto.UserHistoryDto;
import murkeev.currencyexchangerapi.service.HistoryConversationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/history")
@AllArgsConstructor
public class HistoryConversionsController {
    private final HistoryConversationService historyConversationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-history/{id}")
    public Page<UserHistoryDto> getUserHistoryConversations(@PathVariable(value = "id") Long userId,
                                                            @RequestParam(value = "page_number") int pageNumber,
                                                            @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.getUserConversionHistory(userId, PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order-by/{value}")
    public Page<UserHistoryDto> orderByCurrencyValue(@PathVariable String value,
                                                     @RequestParam(value = "page_number") int pageNumber,
                                                     @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.orderBy(value, PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<UserHistoryDto> getUserHistoryConversation(@RequestParam(value = "page_number") int pageNumber,
                                                           @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.getUserHistoryConversation(PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-history/{id}")
    public ResponseEntity<Void> removeHistory(@PathVariable Long id) {
        historyConversationService.removeHistory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createHistory(@RequestBody UserHistoryDto userHistoryDto) {
        historyConversationService.createHistory(userHistoryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> updateHistory(@RequestBody HistoryUpdateDto historyUpdateDto) {
        historyConversationService.updateHistory(historyUpdateDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/find-history/{id}")
    public UserHistoryDto findById(@PathVariable Long id) {
        return historyConversationService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date")
    public Page<UserHistoryDto> findByDate(@RequestParam LocalDate date,
                                           @RequestParam(value = "page_number") int pageNumber,
                                           @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.findByDate(date, PageRequest.of(pageNumber, pageSize));
    }
}
