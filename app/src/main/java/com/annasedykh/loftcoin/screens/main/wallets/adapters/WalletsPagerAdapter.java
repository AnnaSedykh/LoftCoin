package com.annasedykh.loftcoin.screens.main.wallets.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.db.model.QuoteEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.data.model.Currency;
import com.annasedykh.loftcoin.data.model.Fiat;
import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.utils.CurrencyFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletsPagerAdapter extends PagerAdapter {

    private List<WalletEntity> wallets = Collections.emptyList();

    private Prefs prefs;

    public WalletsPagerAdapter(Prefs prefs) {
        this.prefs = prefs;
    }

    public void setWallets(List<WalletEntity> wallets) {
        this.wallets = wallets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_wallet, container, false);

        WalletViewHolder viewHolder = new WalletViewHolder(view, prefs);
        viewHolder.bind(wallets.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return wallets.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    static class WalletViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.symbol_text)
        TextView symbolText;
        @BindView(R.id.symbol_icon)
        ImageView symbolIcon;
        @BindView(R.id.currency_name)
        TextView currency;
        @BindView(R.id.crypto_amount)
        TextView cryptoAmount;
        @BindView(R.id.fiat_amount)
        TextView fiatAmount;

        private Random rand = new Random();
        private Context context;
        private Prefs prefs;

        private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

        private static int[] colors = {
                0xFFF5FF30,
                0xFFFFFFFF,
                0xFF2ABDF5,
                0xFFFF7416,
                0xFFFF7416,
                0xFF534FFF
        };


        WalletViewHolder(View view, Prefs prefs) {
            super(view);
            ButterKnife.bind(this, view);

            this.prefs = prefs;
        }

        public void bind(WalletEntity wallet) {
            bindCurrency(wallet);
            bindSymbol(wallet);
            bindCryptoAmount(wallet);
            bindFiatAmount(wallet);

        }

        private void bindCurrency(WalletEntity wallet) {
            currency.setText(wallet.coin.symbol);
        }

        private void bindSymbol(WalletEntity wallet) {
            Currency currency = Currency.getCurrency(wallet.coin.symbol);
            if(currency != null){
                symbolIcon.setVisibility(View.VISIBLE);
                symbolText.setVisibility(View.INVISIBLE);

                symbolIcon.setImageResource(currency.iconRes);
            }else {
                symbolIcon.setVisibility(View.INVISIBLE);
                symbolText.setVisibility(View.VISIBLE);

                Drawable background = symbolText.getBackground();
                Drawable wrapped = DrawableCompat.wrap(background);
                DrawableCompat.setTint(wrapped, colors[rand.nextInt(colors.length)]);

                symbolText.setText(String.valueOf(wallet.coin.symbol.charAt(0)));
            }
        }

        private void bindCryptoAmount(WalletEntity wallet) {
            String value = currencyFormatter.format(wallet.amount, true);
            cryptoAmount.setText(itemView.getContext().getString(
                    R.string.currency_amount, value, wallet.coin.symbol
            ));
        }

        private void bindFiatAmount(WalletEntity wallet) {
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quote = wallet.coin.getQuote(fiat);

            double amount = wallet.amount * quote.price;
            String value = currencyFormatter.format(amount, false);

            fiatAmount.setText(itemView.getContext().getString(
                    R.string.currency_amount, value, fiat.symbol
            ));
        }
    }
}
