package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Card;
import ru.moiseenko.repository.CardRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService{
    @Autowired
    CardRepository cardRepository;
    public Card findCardById(int cardId){
        Optional<Card> cardFromDb = cardRepository.findById(cardId);
        return cardFromDb.orElse(new Card());
    }
    public List<Card> allCards() {
        return (List<Card>) cardRepository.findAll();
    }
    public boolean deleteCard(int cardId) {
        if (cardRepository.findById(cardId).isPresent()) {
            cardRepository.deleteById(cardId);
            return true;
        }
        return false;
    }
    public boolean saveCard(Card card) {
        if (card == null || card.getPriceCard()<0 || card.getNameCard().length()!=16 || card.getPasswordCard().length()!=3) {
            return false;
        }
        cardRepository.save(card);
        return true;
    }
    @Override
    public Card findCardByNameCard(String nameCard){
        Card cardFromDb = cardRepository.findByNameCard(nameCard);
        if (cardFromDb == null) {
            return new Card();
        }
        return cardFromDb;
    }
    @Override
    public boolean updateCard(Card card) {
        if (card == null || card.getPriceCard()<0 || card.getNameCard().length()!=16 || card.getPasswordCard().length()!=3) {
            return true;
        }
        Card cardToUpdate = cardRepository.findById(card.getId()).get();
        cardToUpdate.setDateCard(card.getDateCard());
        cardToUpdate.setNameCard(card.getNameCard());
        cardToUpdate.setPasswordCard(card.getPasswordCard());
        cardToUpdate.setPriceCard(card.getPriceCard());
        cardRepository.updateCard(cardToUpdate.getId(),cardToUpdate.getPriceCard());
        return false;
    }
}
