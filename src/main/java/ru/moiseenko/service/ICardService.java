package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Card;

@Service
public interface ICardService {
    boolean updateCard(Card card);
    Card findCardByNameCard(String nameCard);
}
