package murkeev.currencyexchangerapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get user's conversion history by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-history/{id}")
    public Page<UserHistoryDto> getUserHistoryConversations(@PathVariable(value = "id") Long userId,
                                                            @RequestParam(value = "page_number") int pageNumber,
                                                            @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.getUserConversionHistory(userId, PageRequest.of(pageNumber, pageSize));
    }

    @Operation(summary = "Order history by currency value",
            description = "Orders the history data by a specified currency value (e.g., base or target), with pagination.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order-by/{value}")
    public Page<UserHistoryDto> orderByCurrencyValue(@PathVariable String value,
                                                     @RequestParam(value = "page_number") int pageNumber,
                                                     @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.orderBy(value, PageRequest.of(pageNumber, pageSize));
    }

    @Operation(summary = "Get the current user's conversion history",
            description = "Returns the logged-in user's conversion history with pagination.")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<UserHistoryDto> getUserHistoryConversation(@RequestParam(value = "page_number") int pageNumber,
                                                           @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.getUserHistoryConversation(PageRequest.of(pageNumber, pageSize));
    }

    @Operation(summary = "Delete a user's history")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-history/{id}")
    public ResponseEntity<Void> removeHistory(@PathVariable Long id) {
        historyConversationService.removeHistory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Create a new history record")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createHistory(@RequestBody UserHistoryDto userHistoryDto) {
        historyConversationService.createHistory(userHistoryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing history record")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> updateHistory(@RequestBody HistoryUpdateDto historyUpdateDto) {
        historyConversationService.updateHistory(historyUpdateDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Find a history record by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/find-history/{id}")
    public UserHistoryDto findById(@PathVariable Long id) {
        return historyConversationService.findById(id);
    }

    @Operation(summary = "Finds all history records for a specific date")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date")
    public Page<UserHistoryDto> findByDate(@RequestParam LocalDate date,
                                           @RequestParam(value = "page_number") int pageNumber,
                                           @RequestParam(value = "page_size") int pageSize) {
        return historyConversationService.findByDate(date, PageRequest.of(pageNumber, pageSize));
    }
}
