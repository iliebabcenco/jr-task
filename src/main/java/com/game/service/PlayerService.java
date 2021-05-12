package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.PageRequest;

import java.sql.SQLException;
import java.util.List;

public interface PlayerService {

    boolean savePlayer(Player p) throws SQLException;
    Player updatePlayer(long id, Player p) throws SQLException;
    List<Player> findAll(PlayerOrder order, PageRequest pg);
    Player getPlayerById(long id) throws SQLException;
    boolean removePlayer(long id) throws SQLException;
    List<Player> findPlayerByStatusAndMinMaxLevel(boolean banned, int minLevel, int maxLevel, PlayerOrder order, PageRequest pg);
    List<Player> findPlayerByBirthdayAndMinMaxExperience(Long after, Long before, int minExperience, int maxExperience, PlayerOrder order, PageRequest pg);
    List<Player> findPlayerByNameAndPageNumber(String name, PlayerOrder order, PageRequest pg);
    List<Player> findPlayerByTitleAndPageSize(String title, PlayerOrder order, PageRequest pg);
    List<Player> findPlayerByRaceProfessionMinMaxExperience(Race race, Profession profession, int minExperience, int maxExperience, PlayerOrder order, PageRequest pg);
    List<Player> findPlayerByRaceProfessionAfterBefore(Race race, Profession profession, Long after, Long before, PlayerOrder order, PageRequest pg);
    Integer countPlayerMinLevelMinExperience(int minLevel, int minExperience);
    Integer countPlayerNameAfterMaxLevel(String name, Long after, int maxLevel);
    Integer countPlayerByTitle(String title);
    Integer countPlayerByRaceProfessionMaxExperience(Race race, Profession profession, int maxExperience);
    Integer countPlayerByRaceProfessionBanned(Race race, Profession profession, boolean banned);
    Integer countPlayerByRaceProfessionBefore(Race race, Profession profession, Long before);
    Integer countPlayerByBanned(boolean banned);
    Integer countAllPlayers();
}
