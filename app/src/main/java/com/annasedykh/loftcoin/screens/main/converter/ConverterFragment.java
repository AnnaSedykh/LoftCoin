package com.annasedykh.loftcoin.screens.main.converter;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.model.Currency;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ConverterFragment extends Fragment {

    @BindView(R.id.converter_toolbar)
    Toolbar toolbar;

    @BindView(R.id.source_currency)
    ViewGroup sourceCurrency;

    @BindView(R.id.source_amount)
    EditText sourceAmount;

    @BindView(R.id.destination_currency)
    ViewGroup destinationCurrency;

    @BindView(R.id.destination_amount)
    TextView destinationAmount;

    TextView sourceCurrencySymbolText;
    ImageView sourceCurrencySymbolIcon;
    TextView sourceCurrencySymbolName;

    TextView destinationCurrencySymbolText;
    ImageView destinationCurrencySymbolIcon;
    TextView destinationCurrencySymbolName;

    private ConverterViewModel viewModel;

    private Unbinder unbinder;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        sourceCurrencySymbolText = sourceCurrency.findViewById(R.id.symbol_text);
        sourceCurrencySymbolIcon = sourceCurrency.findViewById(R.id.symbol_icon);
        sourceCurrencySymbolName = sourceCurrency.findViewById(R.id.currency_name);

        destinationCurrencySymbolText = destinationCurrency.findViewById(R.id.symbol_text);
        destinationCurrencySymbolIcon = destinationCurrency.findViewById(R.id.symbol_icon);
        destinationCurrencySymbolName = destinationCurrency.findViewById(R.id.currency_name);

        toolbar.setTitle(R.string.converter_screen_title);

        Database database = ((App) getActivity().getApplication()).getDatabase();
        viewModel = new ConverterViewModelImpl(savedInstanceState, database);

        initOutputs();
        initInputs();

    }

    private void initOutputs() {

        Disposable disposable = RxTextView.afterTextChangeEvents(sourceAmount)
                .subscribe(textView ->
                        viewModel.onSourceAmountChange(textView.editable().toString()));

        disposables.add(disposable);
    }

    private void initInputs() {

        Disposable disposable1 = viewModel.sourceCurrency().subscribe(currency ->
                bindCurrency(currency, sourceCurrencySymbolIcon, sourceCurrencySymbolText, sourceCurrencySymbolName)
        );

        Disposable disposable2 = viewModel.destinationCurrency().subscribe(currency ->
                bindCurrency(currency, destinationCurrencySymbolIcon, destinationCurrencySymbolText, destinationCurrencySymbolName)
        );

        Disposable disposable3 = viewModel.destinationAmount().subscribe(amount ->
                destinationAmount.setText(amount)
        );

        disposables.addAll(disposable1, disposable2, disposable3);

    }

    private Random rand = new Random();

    private static int[] colors = {
            0xFFF5FF30,
            0xFFFFFFFF,
            0xFF2ABDF5,
            0xFFFF7416,
            0xFFFF7416,
            0xFF534FFF
    };

    private void bindCurrency(String curr, ImageView symbolIcon, TextView symbolText, TextView currencyName) {
        Currency currency = Currency.getCurrency(curr);

        if(currency != null){
            symbolIcon.setVisibility(View.VISIBLE);
            symbolText.setVisibility(View.GONE);

            symbolIcon.setImageResource(currency.iconRes);
        }else {
            symbolIcon.setVisibility(View.GONE);
            symbolText.setVisibility(View.VISIBLE);

            Drawable background = symbolText.getBackground();
            Drawable wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, colors[rand.nextInt(colors.length)]);

            symbolText.setText(String.valueOf(curr.charAt(0)));
        }

        currencyName.setText(curr);
    }


    @Override
    public void onDestroy() {
        unbinder.unbind();
        disposables.dispose();
        super.onDestroy();
    }
}
