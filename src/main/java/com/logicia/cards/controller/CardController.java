package com.logicia.cards.controller;

import com.logicia.cards.dto.CreateCardRequest;
import com.logicia.cards.model.Card;
import com.logicia.cards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/cards")
public class CardController {
    @Autowired
    CardService cardService;
    @Operation(summary = "Add New Card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new card",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    public ResponseEntity<?> addCard(Authentication authentication,@RequestBody CreateCardRequest card){
        String name =authentication.getName();
        System.out.println(name);
        return cardService.addCard(name,card);

    }

    @Operation(summary = "Update Card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card Updated",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})
    @PutMapping("/{cardId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    public ResponseEntity<?> updateCard(Authentication authentication, @RequestBody CreateCardRequest card){
        String name =authentication.getName();
        return cardService.addCard(name,card);

    }

    @Operation(summary = "List Cards Created By Currently logged in member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of All Cards For this User",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})
    @GetMapping("/member")
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    public ResponseEntity<?> listAllCardsForMember(Authentication authentication,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id")  String column,
                                    @RequestParam(defaultValue = "asc")  String sortOrder){
        String currentUserEmail=authentication.getName();
        if(sortOrder.equalsIgnoreCase("asc")){
            PageRequest pageable = PageRequest.of(page, size, Sort.by(column).ascending());
            return cardService.findAllCardsForMember(currentUserEmail,pageable);
        }else if(sortOrder.equalsIgnoreCase("desc")){
            PageRequest pageable = PageRequest.of(page, size, Sort.by(column).descending());
            return cardService.findAllCardsForMember(currentUserEmail,pageable);
        }else{
            return new ResponseEntity<>("bad sort order", HttpStatus.BAD_REQUEST);
        }

    }
    @Operation(summary = "List of All Cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of All Cards",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> listAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id")  String column,
                                     @RequestParam(defaultValue = "asc")  String sortOrder){
        if(sortOrder.equalsIgnoreCase("asc")){
            PageRequest pageable = PageRequest.of(page, size, Sort.by(column).ascending());
            return cardService.getAllCards(pageable);
        }else if(sortOrder.equalsIgnoreCase("desc")){
            PageRequest pageable = PageRequest.of(page, size, Sort.by(column).descending());
            return cardService.getAllCards(pageable);
        }else{
            return new ResponseEntity<>("bad sort order", HttpStatus.BAD_REQUEST);
        }



    }
    @Operation(summary = "Find Card By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card Found",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    @GetMapping("/{cardId}")
    public ResponseEntity<?> findCardById(Authentication authentication,@PathVariable long cardId){
        String name =authentication.getName();
        return cardService.findCardById(name,cardId);

    }

    @Operation(summary = "Delete Card By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card Deleted Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})


    @DeleteMapping("/{cardsId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    public ResponseEntity<?> deleteById(Authentication authentication, @PathVariable long id){
        String name =authentication.getName();
        return cardService.deleteById(name,id);

    }
    // filter by name, status, creation date, color
    @Operation(summary = "Filter using name, status, creation date and color")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filtered Successfully",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = Card.class))}),
            @ApiResponse(responseCode = "401",description = "Unauthorized user",content = @Content),
            @ApiResponse(responseCode = "404",description = "not found",content = @Content),
            @ApiResponse(responseCode = "400",description = "Bad request",content = @Content)})
    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    public ResponseEntity<?> filterByParameter(Authentication authentication,
                                               @RequestParam(defaultValue = "name") String filterColumn,
                                               @RequestParam(defaultValue = "name") String value,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id")  String column,
                                               @RequestParam(defaultValue = "asc")  String sortOrder){
        String currentUserEmail =authentication.getName();
        if(sortOrder.equalsIgnoreCase("asc")){
            PageRequest pageable = PageRequest.of(page, size, Sort.by(column).ascending());
            return cardService.filterByParameter(currentUserEmail,filterColumn,value,pageable);
        }else if(sortOrder.equalsIgnoreCase("desc")){
            PageRequest pageable = PageRequest.of(page, size, Sort.by(column).descending());
            return cardService.filterByParameter(currentUserEmail,filterColumn,value,pageable);
        }else{
            return new ResponseEntity<>("bad sort order", HttpStatus.BAD_REQUEST);
        }

    }

}
