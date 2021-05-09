package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository repository;

    public PlayerServiceImpl() {

    }

    @Autowired
    public PlayerServiceImpl(PlayerRepository repository) {
        super();
        this.repository = repository;
    }


    @Override
    public boolean savePlayer(Player p) throws SQLException {

        if (p.getName().isEmpty() || p.getTitle().isEmpty() || p.getRace().name().isEmpty()
        || p.getProfession().name().isEmpty() || p.getBirthday().getTime() < 0 || p.getExperience() < 0 || p.getExperience() > 10000000) {
            throw new SQLException();
        } else {
            if (p.getExperience() > 0) {
                p.setLevel((int) Math.floor((((Math.sqrt(2500 + 200 * p.getExperience())) - 50) / 100)));
                p.setUntilNextLevel(50 * (p.getLevel() + 1) * (p.getLevel() + 2) - p.getExperience());
            }
            repository.save(p);
            return true;
        }

    }

    @Override
    public boolean updatePlayer(long id, Player p) throws SQLException {
        if (p.getName() == null && p.getTitle() == null && p.getRace() == null
                && p.getProfession() == null && p.getBirthday() == null && !p.isBanned() &&
                p.getExperience() == 0 && p.getLevel() == 0 && p.getUntilNextLevel() == 0) {
            return false;
        }
        Player player = getPlayerById(id);
        List<Race> raceValues = Arrays.asList(Race.values());
        List<Profession> profValues = Arrays.asList(Profession.values());

        if (player == null) {
            throw new NoSuchElementException("player is null");
        }
        if (player.getId() <= 0) {
            throw new NoSuchElementException("player is null");
        }

        if (p.getName() != null && (p.getName().length() <= 0 || p.getName().length() > 12)) {
            throw new SQLException("Invalid length for name");
        } else if (p.getName() != null) {
            player.setName(p.getName());
        }

        if (p.getTitle() != null && (p.getTitle().length() <= 0 || p.getTitle().length() > 12)) {
            throw new SQLException("Invalid value for title");
        } else if (p.getTitle() != null) {
            player.setTitle(p.getTitle());
        }

        if (p.getRace() != null && (!raceValues.contains(p.getRace()))) {
            throw new SQLException("Invalid value for race");
        } else if (p.getRace() != null) {
            player.setRace(p.getRace());
        }

        if (p.getProfession() != null && (!profValues.contains(p.getProfession()))) {
            throw new SQLException("Invalid value for profession");
        } else if (p.getProfession() != null) {
            player.setProfession(p.getProfession());
        }

        if (p.isBanned()) {
            player.setBanned(true);
        } else {
            player.setBanned(false);
        }
        try {
            if (p.getBirthday() != null && (p.getBirthday().compareTo(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01")) < 0
                    || p.getBirthday().compareTo(new SimpleDateFormat("yyyy-MM-dd").parse("3000-01-01")) > 0
            )) {
                throw new SQLException("Invalid value for birthday");
            } else if (p.getBirthday() != null) {
                player.setBirthday(p.getBirthday());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (p.getExperience() < 0 || p.getExperience() > 10000000) {
            throw new SQLException("Invalid value for experience");
        } else if (p.getExperience() > 0) {
            player.setExperience(p.getExperience());
            player.setLevel((int) Math.floor((((Math.sqrt(2500 + 200 * p.getExperience())) - 50) / 100)));
            player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
        }
        repository.save(player);
        return true;

    }

    @Override
    public List<Player> findAll(PlayerOrder order, PageRequest pg) {
        return repository.findAll(Sort.by(order.getFieldName()), pg);
    }

    @Override
    public Player getPlayerById(long id) throws SQLException, InvalidDataAccessApiUsageException {
        if (id <= 0)
            throw new InvalidDataAccessApiUsageException("Error id <= 0");
        if (!repository.findById(id).isPresent())
            throw new NoSuchElementException("no such player in db with id="+id);
         if (repository.findById(id).isPresent()) {
             return repository.findById(id).get();
        } else {
             throw new SQLException("Something wrong with getting player with id="+id);
         }
    }

    @Override
    public boolean removePlayer(long id) throws SQLException {
        if (id <= 0)
            throw new InvalidDataAccessApiUsageException("Error id <= 0");
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            throw new SQLException();
        }
    }

    @Override
    public List<Player> findPlayerByStatusAndMinMaxLevel(boolean banned, int minLevel, int maxLevel, PlayerOrder order, PageRequest pg) {
        return repository.findPlayerByStatusAndMinMaxLevel(banned, minLevel, maxLevel, Sort.by(order.getFieldName()), pg);
    }

    @Override
    public List<Player> findPlayerByBirthdayAndMinMaxExperience(Long after, Long before, int minExperience, int maxExperience, PlayerOrder order, PageRequest pg) {
        Date dateAfter = new Date(after);
        Date dateBefore = new Date(before);
        return repository.findPlayerByBirthdayAndMinMaxExperience(dateAfter, dateBefore, minExperience, maxExperience, Sort.by(order.getFieldName()), pg);
    }

    @Override
    public List<Player> findPlayerByNameAndPageNumber(String name, PlayerOrder order, PageRequest pg) {
        return repository.findPlayerByNameAndPageNumber(name, Sort.by(order.getFieldName()), pg);
    }

    @Override
    public List<Player> findPlayerByTitleAndPageSize(String title, PlayerOrder order, PageRequest pg) {
        return repository.findPlayerByTitleAndPageSize(title, Sort.by(order.getFieldName()), pg);
    }

    @Override
    public List<Player> findPlayerByRaceProfessionMinMaxExperience(Race race, Profession profession, int minExperience, int maxExperience, PlayerOrder order, PageRequest pg) {
        return repository.findPlayerByRaceProfessionMinMaxExperience(race, profession, minExperience, maxExperience, Sort.by(order.getFieldName()), pg);
    }

    @Override
    public List<Player> findPlayerByRaceProfessionAfterBefore(Race race, Profession profession, Long after, Long before, PlayerOrder order, PageRequest pg) {
        Date dateAfter = new Date(after);
        Date dateBefore = new Date(before);
        return repository.findPlayerByRaceProfessionAfterBefore(race, profession, dateAfter, dateBefore, Sort.by(order.getFieldName()), pg);
    }

    @Override
    public Integer countPlayerMinLevelMinExperience(int minLevel, int minExperience) {
        return repository.countPlayerMinLevelMinExperience(minLevel, minExperience);
    }

    @Override
    public Integer countPlayerNameAfterMaxLevel(String name, Long after, int maxLevel) {
        Date dateAfter = new Date(after);
        return repository.countPlayerNameAfterMaxLevel(name, dateAfter, maxLevel);
    }

    @Override
    public Integer countPlayerByTitle(String title) {
        return repository.countPlayerByTitle(title);
    }

    @Override
    public Integer countPlayerByRaceProfessionMaxExperience(Race race, Profession profession, int maxExperience) {
        return repository.countPlayerByRaceProfessionMaxExperience(race, profession, maxExperience);
    }

    @Override
    public Integer countPlayerByRaceProfessionBanned(Race race, Profession profession, boolean banned) {
        return repository.countPlayerByRaceProfessionBanned(race, profession, banned);
    }

    @Override
    public Integer countPlayerByRaceProfessionBefore(Race race, Profession profession, Long before) {
        Date dateBefore = new Date(before);
        return repository.countPlayerByRaceProfessionBefore(race, profession, dateBefore);
    }

    @Override
    public Integer countPlayerByBanned(boolean banned) {
        return repository.countPlayerByBanned(banned);
    }

    @Override
    public Integer countAllPlayers() {
        return repository.countAllPlayers();
    }
}
