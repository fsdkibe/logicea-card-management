package com.logicia.cards.service;

import com.logicia.cards.dto.CreateCardRequest;
import com.logicia.cards.dto.ResponseDto;
import com.logicia.cards.model.Card;
import com.logicia.cards.repository.CardRepository;
import com.logicia.cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardService {
    private final  CardRepository cardRepository;
    private final   UserRepository userRepository;

    @Autowired
    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    ResponseDto responseDto;

    public ResponseEntity<?> addCard(String email, CreateCardRequest createCardRequest){
        responseDto =new ResponseDto();
        try{

            if(createCardRequest.getColor()!=null& createCardRequest.getColor().charAt(0)!='#'|| createCardRequest.getColor().length()!=6){
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                responseDto.setDescription("Card Color should begin with # and be 6 alphanumeric characters");

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }

            Card card= new Card();
            card.setColor(createCardRequest.getColor());
            card.setDescription(createCardRequest.getDescription());
            card.setName(createCardRequest.getName());
            card.setStatus("To Do");
            card.setCreationDate(LocalDate.now().toString());
            card.setUser(userRepository.findByEmail(email).get());
            System.out.println(":::::::::::::::::::::::::::::::::");
            cardRepository.save(card);
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("Card Created Successfully");

            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription(e.toString());

            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> findCardById(String email,long id){
        responseDto =new ResponseDto();

        try{
            if(cardRepository.findById(id).get().getUser().equals(userRepository.findByEmail(email).get())){
                return new ResponseEntity<>( cardRepository.findById(id).get(),HttpStatus.OK);
            }else{
                responseDto.setStatus(HttpStatus.NOT_FOUND);
                responseDto.setDescription("A Card with provided Id is not available in your list");
                return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);

            }



        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Card Not Found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }
   public ResponseEntity<?> getAllCards(Pageable pageable) {
        responseDto=new ResponseDto();
     try {
         return new ResponseEntity<>(cardRepository.findAll(pageable), HttpStatus.FOUND);
     }catch(Exception e){
         responseDto.setStatus(HttpStatus.BAD_REQUEST);
         responseDto.setDescription(e.toString());
         return new ResponseEntity<>(responseDto,responseDto.getStatus());
     }
   }

    public ResponseEntity<?> deleteById( String email, long id){
        responseDto =new ResponseDto();

        try{

            if(cardRepository.findById(id).get().getUser().equals(userRepository.findByEmail(email).get())){
                cardRepository.deleteById(id);
                responseDto.setStatus(HttpStatus.ACCEPTED);
                responseDto.setDescription("Card deleted successfully");
                return new ResponseEntity<>(responseDto,HttpStatus.OK);
            }else{
                responseDto.setStatus(HttpStatus.NOT_FOUND);
                responseDto.setDescription("A Card with provided Id is not available in your list");
                return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
            }



        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Card with that id not found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> findAllCardsForMember(String email,Pageable pageable) {
       return new ResponseEntity<>(cardRepository.findByUser(userRepository.findByEmail(email).get(),pageable),HttpStatus.OK) ;
    }
    // filter by name, color, status, Creation Date
      public ResponseEntity<?> filterByParameter(String email, String parameter, String value, PageRequest pageable) {
        responseDto= new ResponseDto();
        if(parameter.equalsIgnoreCase("name")){
            return new ResponseEntity<>(cardRepository.filterByName(value,userRepository.findByEmail(email).get(),pageable),HttpStatus.OK) ;
        }else if(parameter.equalsIgnoreCase("color")){
            String color="#"+value;
            return new ResponseEntity<>(cardRepository.filterByColor(color,userRepository.findByEmail(email).get(),pageable),HttpStatus.OK) ;
        }else if(parameter.equalsIgnoreCase("creationDate")){

            return new ResponseEntity<>(cardRepository.filterByCreationDate(value,userRepository.findByEmail(email).get(),pageable),HttpStatus.OK) ;
        }else if(parameter.equalsIgnoreCase("status")){
            return new ResponseEntity<>(cardRepository.filterByStatus(value,userRepository.findByEmail(email).get(),pageable),HttpStatus.OK) ;
        }else{
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("Wrong Filter Parameter");
            return new ResponseEntity<>(responseDto,responseDto.getStatus()) ;
        }

    }

}
