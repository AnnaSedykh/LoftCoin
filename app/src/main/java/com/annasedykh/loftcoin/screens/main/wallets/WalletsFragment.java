package com.annasedykh.loftcoin.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.annasedykh.loftcoin.screens.currencies.CurrenciesBottomSheetListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.new_wallet)
    ViewGroup newWallet;

    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    private Unbinder unbinder;

    private WalletsViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.wallets_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);

        Fragment bottomSheet = getFragmentManager().findFragmentByTag(CurrenciesBottomSheet.TAG);
        if(bottomSheet != null){
            ((CurrenciesBottomSheet)bottomSheet).setListener(this);
        }

        viewModel.getWallets();

        initOutputs();
        initInputs();
    }

    private void initOutputs() {
        newWallet.setOnClickListener(view -> viewModel.onNewWalletClick());

        toolbar.getMenu().findItem(R.id.menu_item_add_wallet)
                .setOnMenuItemClickListener(item -> {
                    viewModel.onNewWalletClick();
                    return true;
                });
    }

    private void initInputs() {
        viewModel.selectCurrency().observe(this, o -> {
            showCurrencyBottomSheet();
        });
    }

    private void showCurrencyBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);

    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModel.onCurrencySelected(coin);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
