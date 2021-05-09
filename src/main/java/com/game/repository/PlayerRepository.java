package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository <Player, Long> {
    @Query("SELECT u FROM Player u WHERE u.banned = ?1 and u.level > ?2 and u.level < ?3")
    List<Player> findPlayerByStatusAndMinMaxLevel(boolean banned, int minLevel, int maxLevel, Sort sort, PageRequest pg);

    @Query("SELECT u FROM Player u WHERE u.birthday > ?1 and u.birthday < ?2 and u.experience > ?3 and u.experience < ?4")
    List<Player> findPlayerByBirthdayAndMinMaxExperience(Date after, Date before, int minExperience, int maxExperience, Sort sort, PageRequest pg);

    @Query("SELECT u FROM Player u WHERE u.name like %?1%")
    List<Player> findPlayerByNameAndPageNumber(String name, Sort sort, PageRequest pg);

    @Query("SELECT u FROM Player u")
    List<Player> findAll(Sort sort, PageRequest pg);

    @Query("SELECT u FROM Player u WHERE u.title like %?1%")
    List<Player> findPlayerByTitleAndPageSize(String title, Sort sort, PageRequest pg);

    @Query("SELECT u FROM Player u WHERE u.race = ?1 and u.profession = ?2 and u.experience > ?3 and u.experience < ?4")
    List<Player> findPlayerByRaceProfessionMinMaxExperience(Race race, Profession profession, int minExperience, int maxExperience, Sort sort, PageRequest pg);

    @Query("SELECT u FROM Player u WHERE u.race = ?1 and u.profession = ?2 and u.birthday > ?3 and u.birthday < ?4")
    List<Player> findPlayerByRaceProfessionAfterBefore(Race race, Profession profession, Date after, Date before, Sort sort, PageRequest pg);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.level > ?1 and u.experience > ?2")
    Integer countPlayerMinLevelMinExperience(int minLevel, int minExperience);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.name like %?1% and u.birthday > ?2 and u.level < ?3")
    Integer countPlayerNameAfterMaxLevel(String name, Date after, int maxLevel);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.title like %?1%")
    Integer countPlayerByTitle(String title);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.race = ?1 and u.profession = ?2 and u.experience < ?3")
    Integer countPlayerByRaceProfessionMaxExperience(Race race, Profession profession, int maxExperience);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.race = ?1 and u.profession = ?2 and u.banned = ?3")
    Integer countPlayerByRaceProfessionBanned(Race race, Profession profession, boolean banned);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.race = ?1 and u.profession = ?2 and u.birthday < ?3")
    Integer countPlayerByRaceProfessionBefore(Race race, Profession profession, Date beforeDate);

    @Query("SELECT COUNT(u) FROM Player u WHERE u.banned = ?1")
    Integer countPlayerByBanned(boolean banned);

    @Query("SELECT COUNT(u) FROM Player u")
    Integer countAllPlayers();


}
