package com.mali.api;

import com.mali.api.json.RestResponseJSON;
import com.mali.coin.api.CoinHistoryService;
import com.mali.persistence.entity.Coin;
import com.mali.persistence.entity.CoinHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoinHistoryRestful {

    @Autowired
    private CoinHistoryService coinHistoryService;

    @RequestMapping("/coin-history/{idIcon}")
    public ResponseEntity<RestResponseJSON> listByIdicon(@PathVariable("idIcon") String idIcon, @RequestParam int requiredHistorySize) {

        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<CoinHistory> history = coinHistoryService.listByIdicon(idIcon,requiredHistorySize);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setResponse(history);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }

    }
}
