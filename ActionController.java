package com.example.calculatorexample.controller;

import com.example.calculatorexample.beans.*;
import com.example.calculatorexample.model.Action;
import com.example.calculatorexample.model.Data;
import com.example.calculatorexample.repository.ActionRepository;
import com.example.calculatorexample.repository.DataRepository;
import com.example.calculatorexample.service.ActionService;
import com.example.calculatorexample.service.DataService;
import com.example.calculatorexample.wsdl.WsdlOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ActionController {

//    @Autowired
//    private ActionRepository actionRepository;
//    @Autowired
//    private DataRepository dataRepository;

    private final static Logger LOG = LogManager.getLogger();
    private static int counter;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM HH:mm:ss");

    /////////////////////////////////////////////
    private ActionService actionService;
    private DataService dataService;

    private Action action;
    private static final String REQUEST_TO_JSON = "Request to JSON";
    private static final String REQUEST_TO_SOAP = "Request to SOAP";
    private static final String REQUEST_FROM_SOAP = "Request from SOAP";

    @Autowired
    public ActionController(ActionService actionService, DataService dataService) {
        this.actionService = actionService;
        this.dataService = dataService;
    }
    //////////////////////////////////////////////

    @PostMapping("/add")
    public AddResponseBean add(@Valid @RequestBody AddRequestBean requestBean) {
        AddResponseBean responseBean = new AddResponseBean();
        counter++;

        String timestampRequestToJSON = formatter.format(new Date());
        postAction();
        postData(timestampRequestToJSON, REQUEST_TO_JSON);

        String timestampRequestToSOAP = formatter.format(new Date());
        postData(timestampRequestToSOAP, REQUEST_TO_SOAP);

        int addResult = WsdlOperation.getInstance().getAddResultFromSoap(requestBean.getIntA(), requestBean.getIntB());
        responseBean.setAddResult(addResult);

        String timestampRequestFromSOAP = formatter.format(new Date());
        postData(timestampRequestFromSOAP, REQUEST_FROM_SOAP);

        return responseBean;
    }

    private void postAction() {
        action = new Action();
        action.setInsertDate(new Date());
        actionService.save(action);
    }

    private void postData(String timestamp, String requestStaticString) {
        LOG.info("[{}] - Call {}. " + requestStaticString, timestamp, counter);
        Data data = new Data(new Date(), "[" + timestamp + "]" + " - Call " + counter + ". " + requestStaticString);
        data.setAction(action);
        dataService.save(data);
    }

//    @PostMapping("/add")
//    public AddResponseBean add(@Valid @RequestBody AddRequestBean requestBean) {
//        AddResponseBean responseBean = new AddResponseBean();
//        counter++;
//
//        LOG.info("[{}] - Call {}. Request to JSON", formatter.format(new Date()), counter);
//        Data data1 = new Data(new Date(), "[" + formatter.format(new Date()) + "]" + " - Call " + counter + ". Request to JSON");
//        Action action = new Action();
//        action.setInsertDate(new Date());
//        actionRepository.save(action);
//
//        data1.setAction(action);
//        dataRepository.save(data1);
//
//        LOG.info("[{}] - Call {}. Request to SOAP", formatter.format(new Date()), counter);
//        Data data2 = new Data(new Date(), "[" + formatter.format(new Date()) + "]" + " - Call " + counter + ". Request to SOAP");
//        data2.setAction(action);
//        dataRepository.save(data2);
//
//        int addResult = WsdlOperation.getInstance().getAddResultFromSoap(requestBean.getIntA(), requestBean.getIntB());
//        responseBean.setAddResult(addResult);
//
//        LOG.info("[{}] - Call {}. Request from SOAP", formatter.format(new Date()), counter);
//        Data data3 = new Data(new Date(), "[" + formatter.format(new Date()) + "]" + " - Call " + counter + ". Request from SOAP");
//        data3.setAction(action);
//        dataRepository.save(data3);
//
//        return responseBean;
//    }

//    @PostMapping("/divide")
//    public DivideResponseBean divide(@Valid @RequestBody DivideRequestBean requestBean) {
//        counter++;
//        Date currentDate = new Date();
//        Timestamp ts = new Timestamp(currentDate.getTime());
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM HH:mm:ss");
//
//        LOG.info("[{}] - Call {}. Request to JSON", formatter.format(ts), counter);
//        DivideResponseBean responseBean = new DivideResponseBean();
//
//        LOG.info("[{}] - Call {}. Request to SOAP", formatter.format(ts), counter);
//        int divideResult = WsdlOperation.getInstance().getDivideResultFromSoap(requestBean.getIntA(), requestBean.getIntB());
//        responseBean.setDivideResult(divideResult);
//
//        LOG.info("[{}] - Call {}. Request from SOAP", formatter.format(ts), counter);
//        return responseBean;
//    }
//
//    @PostMapping("/multiply")
//    public MultiplyResponseBean multiply(@Valid @RequestBody MultiplyRequestBean requestBean) {
//        MultiplyResponseBean responseBean = new MultiplyResponseBean();
//
//        int multiplyResult = WsdlOperation.getInstance().getMultiplyResultFromSoap(requestBean.getIntA(), requestBean.getIntB());
//        responseBean.setMultiplyResult(multiplyResult);
//
//        return responseBean;
//    }
//
//    @PostMapping("/subtract")
//    public SubtractResponseBean subtract(@Valid @RequestBody SubtractRequestBean requestBean) {
//        SubtractResponseBean responseBean = new SubtractResponseBean();
//
//        int subtractResult = WsdlOperation.getInstance().getSubtractResultFromSoap(requestBean.getIntA(), requestBean.getIntB());
//        responseBean.setSubtractResult(subtractResult);
//
//        return responseBean;
//    }

}
