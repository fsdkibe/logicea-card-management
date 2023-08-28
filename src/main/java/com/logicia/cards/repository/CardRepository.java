package com.logicia.cards.repository;

import com.logicia.cards.model.Card;
import com.logicia.cards.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c where c.user = :user")
    List<Card> findByUser(User user, Pageable pageable);
    @Query("SELECT c FROM Card c where c.color = :color and c.user=:user" )
    List<Card> filterByColor(String color,User user,Pageable pageable);
    @Query("SELECT c FROM Card c where c.name = :name and c.user=:user")
    List<Card> filterByName(String name,User user,Pageable pageable);
    @Query("SELECT c FROM Card c where c.status = :status and c.user=:user")
    List<Card> filterByStatus(String status,User user,Pageable pageable);
    @Query("SELECT c FROM Card c where c.creationDate = :date and c.user=:user")
    List<Card> filterByCreationDate(String date,User user,Pageable pageable);

}
