package com.example.calculatorexample.wsdl;

import com.example.calculatorexample.wsdl.org.tempuri.*;

public class WsdlOperation {
    private static WsdlOperation ourInstance = new WsdlOperation();

    public static WsdlOperation getInstance() {
        return ourInstance;
    }

    private WsdlOperation() {
    }

    private CalculatorSoap getClient() {
        Calculator calcServer = new Calculator();
        CalculatorSoap client = calcServer.getCalculatorSoap();

        return client;
    }

    public int getAddResultFromSoap(int intA, int intB) {
        Add request = new Add();
        request.setIntA(intA);
        request.setIntB(intB);

        AddResponse response = new AddResponse();
        int result = getClient().add(request.getIntA(), request.getIntB());
        response.setAddResult(result);

        return response.getAddResult();
    }

    public int getDivideResultFromSoap(int intA, int intB) {
        Divide request = new Divide();
        request.setIntA(intA);
        request.setIntB(intB);

        DivideResponse response = new DivideResponse();
        int result = getClient().divide(request.getIntA(), request.getIntB());
        response.setDivideResult(result);

        return response.getDivideResult();
    }

    public int getMultiplyResultFromSoap(int intA, int intB) {
        Multiply request = new Multiply();
        request.setIntA(intA);
        request.setIntB(intB);

        MultiplyResponse response = new MultiplyResponse();
        int result = getClient().multiply(request.getIntA(), request.getIntB());
        response.setMultiplyResult(result);

        return response.getMultiplyResult();
    }

    public int getSubtractResultFromSoap(int intA, int intB) {
        Subtract request = new Subtract();
        request.setIntA(intA);
        request.setIntB(intB);

        SubtractResponse response = new SubtractResponse();
        int result = getClient().subtract(request.getIntA(), request.getIntB());
        response.setSubtractResult(result);

        return response.getSubtractResult();
    }
}
