package com.crypfy.api;


import com.crypfy.api.json.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BookProcessor {

    //get the book from fox
    public List<OrderBook> getFoxbitBook(){

        FoxbitBook foxbitBook = new FoxbitBook();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FoxbitBook> response = restTemplate.getForEntity("https://api.blinktrade.com/api/v1/BRL/orderbook?crypto_currency=BTC",FoxbitBook.class);
            foxbitBook = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            //return it with negative status
            OrderBook buyOrderBook = new OrderBook();
            OrderBook sellOrderBook = new OrderBook();
            buyOrderBook.setSuccess(false);
            sellOrderBook.setSuccess(false);

            List<OrderBook> orderBooks = new ArrayList<OrderBook>();
            orderBooks.add(buyOrderBook);
            orderBooks.add(sellOrderBook);
            return orderBooks;
        }

        //process it
        OrderBook buyOrderBook = new OrderBook();
        OrderBook sellOrderBook = new OrderBook();
        buyOrderBook.setSuccess(true);
        sellOrderBook.setSuccess(true);
        List<BookDetails> buyBookDetailsList = new ArrayList<BookDetails>();
        List<BookDetails> sellBookDetailsList = new ArrayList<BookDetails>();
        //buy
        for(double [] details : foxbitBook.getBids()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            buyBookDetailsList.add(bookDetails);
        }
        buyOrderBook.setResult(buyBookDetailsList);
        //sell
        for(double [] details : foxbitBook.getAsks()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            sellBookDetailsList.add(bookDetails);
        }
        sellOrderBook.setResult(sellBookDetailsList);

        List<OrderBook> orderBooks = new ArrayList<OrderBook>();
        orderBooks.add(buyOrderBook);
        orderBooks.add(sellOrderBook);

        return orderBooks;
    }
    //get the book from negocie coins
    public List<OrderBook> getNcoinsBook(){
        NCoinsBook nCoinsBook = new NCoinsBook();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<NCoinsBook> response = restTemplate.exchange("https://broker.negociecoins.com.br/api/v3/btcbrl/orderbook", HttpMethod.GET,entity,NCoinsBook.class);
            nCoinsBook = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            //return it with negative status
            OrderBook buyOrderBook = new OrderBook();
            OrderBook sellOrderBook = new OrderBook();
            buyOrderBook.setSuccess(false);
            sellOrderBook.setSuccess(false);

            List<OrderBook> orderBooks = new ArrayList<OrderBook>();
            orderBooks.add(buyOrderBook);
            orderBooks.add(sellOrderBook);
            return orderBooks;
        }

        //process it
        OrderBook buyOrderBook = new OrderBook();
        OrderBook sellOrderBook = new OrderBook();
        buyOrderBook.setSuccess(true);
        sellOrderBook.setSuccess(true);
        List<BookDetails> buyBookDetailsList = new ArrayList<BookDetails>();
        List<BookDetails> sellBookDetailsList = new ArrayList<BookDetails>();
        //buy
        for(NCoinsBookDetails details : nCoinsBook.getBid()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details.getQuantity());
            bookDetails.setRate(details.getPrice());
            //add it
            buyBookDetailsList.add(bookDetails);
        }
        buyOrderBook.setResult(buyBookDetailsList);
        //sell
        for(NCoinsBookDetails details : nCoinsBook.getAsk()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details.getQuantity());
            bookDetails.setRate(details.getPrice());
            //add it
            sellBookDetailsList.add(bookDetails);
        }
        sellOrderBook.setResult(sellBookDetailsList);

        List<OrderBook> orderBooks = new ArrayList<OrderBook>();
        orderBooks.add(buyOrderBook);
        orderBooks.add(sellOrderBook);

        return orderBooks;
    }

    //get the book of ltc from negocie coins
    public List<OrderBook> getNcoinsLTCBook(){
        NCoinsBook nCoinsBook = new NCoinsBook();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<NCoinsBook> response = restTemplate.exchange("https://broker.negociecoins.com.br/api/v3/ltcbrl/orderbook", HttpMethod.GET,entity,NCoinsBook.class);
            nCoinsBook = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            //return it with negative status
            OrderBook buyOrderBook = new OrderBook();
            OrderBook sellOrderBook = new OrderBook();
            buyOrderBook.setSuccess(false);
            sellOrderBook.setSuccess(false);

            List<OrderBook> orderBooks = new ArrayList<OrderBook>();
            orderBooks.add(buyOrderBook);
            orderBooks.add(sellOrderBook);
            return orderBooks;
        }

        //process it
        OrderBook buyOrderBook = new OrderBook();
        OrderBook sellOrderBook = new OrderBook();
        buyOrderBook.setSuccess(true);
        sellOrderBook.setSuccess(true);
        List<BookDetails> buyBookDetailsList = new ArrayList<BookDetails>();
        List<BookDetails> sellBookDetailsList = new ArrayList<BookDetails>();
        //buy
        for(NCoinsBookDetails details : nCoinsBook.getBid()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details.getQuantity());
            bookDetails.setRate(details.getPrice());
            //add it
            buyBookDetailsList.add(bookDetails);
        }
        buyOrderBook.setResult(buyBookDetailsList);
        //sell
        for(NCoinsBookDetails details : nCoinsBook.getAsk()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details.getQuantity());
            bookDetails.setRate(details.getPrice());
            //add it
            sellBookDetailsList.add(bookDetails);
        }
        sellOrderBook.setResult(sellBookDetailsList);

        List<OrderBook> orderBooks = new ArrayList<OrderBook>();
        orderBooks.add(buyOrderBook);
        orderBooks.add(sellOrderBook);

        return orderBooks;
    }

    //get the book from mercado bitcoin
    public List<OrderBook> getMBtcBook(){
        MBtcBook mBtcBook = new MBtcBook();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MBtcBook> response = restTemplate.getForEntity("https://www.mercadobitcoin.net/api/BTC/orderbook/",MBtcBook.class);
            mBtcBook = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            //return it with negative status
            OrderBook buyOrderBook = new OrderBook();
            OrderBook sellOrderBook = new OrderBook();
            buyOrderBook.setSuccess(false);
            sellOrderBook.setSuccess(false);

            List<OrderBook> orderBooks = new ArrayList<OrderBook>();
            orderBooks.add(buyOrderBook);
            orderBooks.add(sellOrderBook);
            return orderBooks;
        }

        //process it
        OrderBook buyOrderBook = new OrderBook();
        OrderBook sellOrderBook = new OrderBook();
        buyOrderBook.setSuccess(true);
        sellOrderBook.setSuccess(true);
        List<BookDetails> buyBookDetailsList = new ArrayList<BookDetails>();
        List<BookDetails> sellBookDetailsList = new ArrayList<BookDetails>();
        //buy
        for( double[] details : mBtcBook.getBids()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            buyBookDetailsList.add(bookDetails);
        }
        buyOrderBook.setResult(buyBookDetailsList);
        //sell
        for(double[] details : mBtcBook.getAsks()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            sellBookDetailsList.add(bookDetails);
        }
        sellOrderBook.setResult(sellBookDetailsList);

        List<OrderBook> orderBooks = new ArrayList<OrderBook>();
        orderBooks.add(buyOrderBook);
        orderBooks.add(sellOrderBook);

        return orderBooks;
    }

    //get the book of ltc from mercado bitcoin
    public List<OrderBook> getMBtcLTCBook(){
        MBtcBook mBtcBook = new MBtcBook();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MBtcBook> response = restTemplate.getForEntity("https://www.mercadobitcoin.net/api/LTC/orderbook/",MBtcBook.class);
            mBtcBook = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            //return it with negative status
            OrderBook buyOrderBook = new OrderBook();
            OrderBook sellOrderBook = new OrderBook();
            buyOrderBook.setSuccess(false);
            sellOrderBook.setSuccess(false);

            List<OrderBook> orderBooks = new ArrayList<OrderBook>();
            orderBooks.add(buyOrderBook);
            orderBooks.add(sellOrderBook);
            return orderBooks;
        }

        //process it
        OrderBook buyOrderBook = new OrderBook();
        OrderBook sellOrderBook = new OrderBook();
        buyOrderBook.setSuccess(true);
        sellOrderBook.setSuccess(true);
        List<BookDetails> buyBookDetailsList = new ArrayList<BookDetails>();
        List<BookDetails> sellBookDetailsList = new ArrayList<BookDetails>();
        //buy
        for( double[] details : mBtcBook.getBids()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            buyBookDetailsList.add(bookDetails);
        }
        buyOrderBook.setResult(buyBookDetailsList);
        //sell
        for(double[] details : mBtcBook.getAsks()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            sellBookDetailsList.add(bookDetails);
        }
        sellOrderBook.setResult(sellBookDetailsList);

        List<OrderBook> orderBooks = new ArrayList<OrderBook>();
        orderBooks.add(buyOrderBook);
        orderBooks.add(sellOrderBook);

        return orderBooks;
    }

    //get the book from bitcoin to you
    public List<OrderBook> getBtcToYouBook(){
        MBtcBook mBtcBook = new MBtcBook();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity("https://www.bitcointoyou.com/api/orderbook.aspx",String.class);
            String jsonfake = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            mBtcBook = mapper.readValue(jsonfake,MBtcBook.class);
        } catch (Exception e) {
            e.printStackTrace();
            //return it with negative status
            OrderBook buyOrderBook = new OrderBook();
            OrderBook sellOrderBook = new OrderBook();
            buyOrderBook.setSuccess(false);
            sellOrderBook.setSuccess(false);

            List<OrderBook> orderBooks = new ArrayList<OrderBook>();
            orderBooks.add(buyOrderBook);
            orderBooks.add(sellOrderBook);
            return orderBooks;
        }

        //process it
        OrderBook buyOrderBook = new OrderBook();
        OrderBook sellOrderBook = new OrderBook();
        buyOrderBook.setSuccess(true);
        sellOrderBook.setSuccess(true);
        List<BookDetails> buyBookDetailsList = new ArrayList<BookDetails>();
        List<BookDetails> sellBookDetailsList = new ArrayList<BookDetails>();
        //buy
        for( double[] details : mBtcBook.getBids()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            buyBookDetailsList.add(bookDetails);
        }
        buyOrderBook.setResult(buyBookDetailsList);
        //sell
        for(double[] details : mBtcBook.getAsks()){
            BookDetails bookDetails = new BookDetails();
            bookDetails.setQuantity(details[1]);
            bookDetails.setRate(details[0]);
            //add it
            sellBookDetailsList.add(bookDetails);
        }
        sellOrderBook.setResult(sellBookDetailsList);

        List<OrderBook> orderBooks = new ArrayList<OrderBook>();
        orderBooks.add(buyOrderBook);
        orderBooks.add(sellOrderBook);

        return orderBooks;
    }


    //general method to get the buy price
    public double getBuyPrice(OrderBook orderBook,double brlAmount){

        //needed variables
        double buyPrice=0,buyAmount=0,buyBalance= brlAmount;
        List<Double> buyRates = new ArrayList<Double>(),buyAmounts = new ArrayList<Double>();
        List<BookDetails> buyOrders = orderBook.getResult();
        boolean buySuccess = orderBook.isSuccess();
        //iterate through the book
        if(buySuccess) {
            for (int i = 0; i < buyOrders.size(); i++) {
                BookDetails bookDetails = buyOrders.get(i);
                double quantity = bookDetails.getQuantity();
                double rate = bookDetails.getRate();
                if (buyBalance / rate < quantity) {
                    buyRates.add(rate);
                    buyAmounts.add(buyBalance / rate);
                    break;
                } else {
                    buyRates.add(rate);
                    buyAmounts.add(quantity);
                    buyBalance = buyBalance - quantity * rate;
                }
                if (i == buyOrders.size() - 1 && buyBalance > 0) {
                    System.out.println("O order-book não suporta essa ordem");
                    return 0;
                }
            }
            //lets do the magic
            if (buyRates.size() < 2) {
                if (buyRates.size() > 0) buyPrice = buyRates.get(0);
            } else {
                for (int i = 0; i < buyRates.size(); i++) {
                    buyPrice = buyPrice + buyRates.get(i) * buyAmounts.get(i);
                    buyAmount = buyAmount + buyAmounts.get(i);
                }
                buyPrice = buyPrice / buyAmount;
            }
        }else return 0;
        return buyPrice;
    }

    //general method to get the sell price
    public double getSellPrice(OrderBook orderBook,double btcAmount){

        //needed variables
        double price=0,amount=0,balance=btcAmount;
        List<Double> rates = new ArrayList<Double>(),amounts = new ArrayList<Double>();
        List<BookDetails> orders = orderBook.getResult();
        boolean success = orderBook.isSuccess();
        //iterate through order book
        if(success) {
            for (int i = 0; i < orders.size(); i++) {
                BookDetails bookDetails = orders.get(i);
                double quant = bookDetails.getQuantity();
                double rate = bookDetails.getRate();
                if (balance * rate < quant) {
                    rates.add(rate);
                    amounts.add(balance * rate);
                    break;
                } else {
                    rates.add(rate);
                    amounts.add(quant * rate);
                    balance = balance - quant;
                }
                if (i == orders.size() - 1 && balance > 0) {
                    System.out.println("O order-book não suporta essa ordem");
                    return 0;
                }
            }
            //do the math!!!
            if (rates.size() < 2) {
                if (rates.size() > 0) price = rates.get(0);
            } else {
                for (int i = 0; i < rates.size(); i++) {
                    price = price + rates.get(i) * amounts.get(i);
                    amount = amount + amounts.get(i);
                }
                price = price / amount;
            }
            return price;
        }else return 0;

    }

}
