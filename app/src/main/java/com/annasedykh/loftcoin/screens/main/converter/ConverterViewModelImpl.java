package com.annasedykh.loftcoin.screens.main.converter;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.utils.CurrencyFormatter;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ConverterViewModelImpl implements ConverterViewModel {

    private static final String KEY_SOURCE_CURRENCY = "source_currency";
    private static final String KEY_DESTINATION_CURRENCY = "destination_currency";


    private BehaviorSubject<String> sourceCurrency = BehaviorSubject.create();
    private BehaviorSubject<String> destinationCurrency = BehaviorSubject.create();
    private BehaviorSubject<String> destinationAmount = BehaviorSubject.create();

    private PublishSubject<Object> selectSourceCurrency = PublishSubject.create();
    private PublishSubject<Object> selectDestinationCurrency = PublishSubject.create();

    private Database database;

    private String sourceAmountValue = "";

    private String sourceCurrencySymbol = "BTC";
    private String destinationCurrencySymbol = "ETH";

    private CoinEntity sourceCoin;
    private CoinEntity destinationCoin;

    private CurrencyFormatter currencyFormatter = new CurrencyFormatter();
    private CompositeDisposable disposables = new CompositeDisposable();

    public ConverterViewModelImpl(Bundle savedInstanceState, Database database) {
        this.database = database;
        database.open();

        if(savedInstanceState != null){
            sourceCurrencySymbol = savedInstanceState.getString(KEY_SOURCE_CURRENCY);
            destinationCurrencySymbol = savedInstanceState.getString(KEY_DESTINATION_CURRENCY);
        }

        loadCoins();
    }

    @SuppressLint("CheckResult")
    private void loadCoins() {

        Disposable disposable1 = Observable.fromCallable(() ->
                database.getCoin(sourceCurrencySymbol))
                .subscribe(this::onSourceCurrencySelected);

        Disposable disposable2 = Observable.fromCallable(() ->
                database.getCoin(destinationCurrencySymbol))
                .subscribe(this::onDestinationCurrencySelected);

        disposables.add(disposable1);
        disposables.add(disposable2);

    }

    @Override
    public Observable<String> sourceCurrency() {
        return sourceCurrency;
    }

    @Override
    public Observable<String> destinationCurrency() {
        return destinationCurrency;
    }

    @Override
    public Observable<String> destinationAmount() {
        return destinationAmount;
    }

    @Override
    public Observable<Object> selectSourceCurrency() {
        return selectSourceCurrency;
    }

    @Override
    public Observable<Object> selectDestinationCurrency() {
        return selectDestinationCurrency;
    }

    @Override
    public void onSourceAmountChange(String amount) {
        sourceAmountValue = amount;

        convert();
    }

    private void convert() {

        if(sourceAmountValue.isEmpty()){
            destinationAmount.onNext("");
            return;
        }

        if (sourceCoin == null || destinationCoin == null) {
            return;
        }

        double sourceValue = Double.parseDouble(sourceAmountValue);
        double destinationValue = sourceValue * sourceCoin.usd.price / destinationCoin.usd.price;
        String destinationAmountValue = String.valueOf(currencyFormatter.formatForConverter(destinationValue));

        destinationAmount.onNext(destinationAmountValue);
    }

    @Override
    public void onSourceCurrencyClick() {
        selectSourceCurrency.onNext(new Object());
    }

    @Override
    public void onDestinationCurrencyClick() {
        selectDestinationCurrency.onNext(new Object());
    }

    @Override
    public void onSourceCurrencySelected(CoinEntity coin) {
        sourceCoin = coin;
        sourceCurrencySymbol = coin.symbol;
        sourceCurrency.onNext(coin.symbol);
        convert();
    }

    @Override
    public void onDestinationCurrencySelected(CoinEntity coin) {
        destinationCoin = coin;
        destinationCurrencySymbol = coin.symbol;
        destinationCurrency.onNext(coin.symbol);
        convert();
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putString(KEY_SOURCE_CURRENCY, sourceCurrencySymbol);
        outState.putString(KEY_DESTINATION_CURRENCY, destinationCurrencySymbol);
    }

    @Override
    public void onDetach() {
        database.close();
    }
}
