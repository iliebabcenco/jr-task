package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RestController
@RequestMapping("/rest")
public class PlayerController {
    private PlayerService playerService;

    public PlayerController() {

    }

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> displayAllPlayer(
            @RequestParam(required = false) String name
            , @RequestParam(required = false) String title
            , @RequestParam(required = false) Race race
            , @RequestParam(required = false) Profession profession
            , @RequestParam(required = false) Long after
            , @RequestParam(required = false) Long before
            , @RequestParam(required = false) Boolean banned
            , @RequestParam(required = false) Integer minExperience
            , @RequestParam(required = false) Integer maxExperience
            , @RequestParam(required = false) Integer minLevel
            , @RequestParam(required = false) Integer maxLevel
            , @RequestParam(required = false) PlayerOrder order
            , @RequestParam(required = false) Integer pageNumber
            , @RequestParam(required = false) Integer pageSize
    ) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }
        if (order == null) {
            order = PlayerOrder.ID;
        }
        try {
            List<Player> respList = new ArrayList<Player>();
            if (after != null && before != null && minExperience != null && maxExperience != null) {
                respList = playerService.findPlayerByBirthdayAndMinMaxExperience(after, before, minExperience, maxExperience, order, PageRequest.of(pageNumber, pageSize));
            } else if (banned != null && minLevel != null && maxLevel != null) {
                respList = playerService.findPlayerByStatusAndMinMaxLevel(banned, minLevel, maxLevel, order, PageRequest.of(pageNumber, pageSize));
            } else if (name != null) {
                respList = playerService.findPlayerByNameAndPageNumber(name, order, PageRequest.of(pageNumber, pageSize));
            } else if (title != null) {
                respList = playerService.findPlayerByTitleAndPageSize(title, order, PageRequest.of(pageNumber, pageSize));
            } else if (race != null && profession != null && minExperience != null && maxExperience != null) {
                respList = playerService.findPlayerByRaceProfessionMinMaxExperience(race, profession, minExperience, maxExperience, order, PageRequest.of(pageNumber, pageSize));
            } else if (banned != null && minLevel == null && maxLevel != null) {
                respList = playerService.findPlayerByStatusAndMinMaxLevel(banned, 0, maxLevel, order, PageRequest.of(pageNumber, pageSize));
            } else if (race != null && profession != null && after != null && before != null) {
                respList = playerService.findPlayerByRaceProfessionAfterBefore(race, profession, after, before, order, PageRequest.of(pageNumber, pageSize));
            } else {
                respList = playerService.findAll(order, PageRequest.of(pageNumber, pageSize));
            }

            return new ResponseEntity<>(respList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/players/count")
    public ResponseEntity<Integer> displayCountOfPlayers(
            @RequestParam(required = false) String name
            , @RequestParam(required = false) String title
            , @RequestParam(required = false) Race race
            , @RequestParam(required = false) Profession profession
            , @RequestParam(required = false) Long after
            , @RequestParam(required = false) Long before
            , @RequestParam(required = false) Boolean banned
            , @RequestParam(required = false) Integer minExperience
            , @RequestParam(required = false) Integer maxExperience
            , @RequestParam(required = false) Integer minLevel
            , @RequestParam(required = false) Integer maxLevel
    ) {
        try {
            Integer counter = 0;
            if (minLevel != null && minExperience != null) {
                counter = playerService.countPlayerMinLevelMinExperience(minLevel, minExperience);
            } else if (name != null && after != null && maxLevel != null) {
                counter = playerService.countPlayerNameAfterMaxLevel(name, after, maxLevel);
            } else if (title != null) {
                counter = playerService.countPlayerByTitle(title);
            } else if (race != null && profession != null && maxExperience != null) {
                counter = playerService.countPlayerByRaceProfessionMaxExperience(race, profession, maxExperience);
            } else if (race != null && profession != null && banned != null) {
                counter = playerService.countPlayerByRaceProfessionBanned(race, profession, banned);
            } else if (race != null && profession != null && before != null) {
                counter = playerService.countPlayerByRaceProfessionBefore(race, profession, before);
            } else if (banned != null) {
                counter = playerService.countPlayerByBanned(banned);
            } else {
                counter = playerService.countAllPlayers();
            }

            return new ResponseEntity<>(counter, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") long id) {
        try {
            Player player = playerService.getPlayerById(id);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (InvalidDataAccessApiUsageException s ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SQLException | NoSuchElementException n) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/players")
    public ResponseEntity<Player> saveNewUser(@RequestBody Player player) {
        try {
            playerService.savePlayer(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updateUser(@PathVariable("id") long id, @RequestBody Player player) {
        try {
            Player upPlayer = playerService.updatePlayer(id, player);
            return new ResponseEntity<>(upPlayer, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (SQLException | InvalidDataAccessApiUsageException i) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/players/{id}")
    public ResponseEntity<HttpStatus> deletePlayerById(@PathVariable Long id) {
        try {
            playerService.removePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidDataAccessApiUsageException n) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SQLException | NoSuchElementException s) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
