package com.annasedykh.loftcoin.screens.main.rate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.QuoteEntity;
import com.annasedykh.loftcoin.data.model.Currency;
import com.annasedykh.loftcoin.data.model.Fiat;
import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.utils.CurrencyFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {

    private List<CoinEntity> coins = Collections.emptyList();

    private Prefs prefs;

    RateAdapter(Prefs prefs) {
        this.prefs = prefs;
    }

    public void setCoins(List<CoinEntity> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate, parent, false);
        return new RateViewHolder(view, prefs);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        holder.bind(coins.get(position), position);
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    @Override
    public long getItemId(int position) {
        return coins.get(position).id;
    }

    static class RateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.symbol_text)
        TextView symbolText;
        @BindView(R.id.symbol_icon)
        ImageView symbolIcon;
        @BindView(R.id.currency_name)
        TextView name;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.percent_change)
        TextView percentChange;

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

        RateViewHolder(View itemView, Prefs prefs) {
            super(itemView);
            this.prefs = prefs;
            context = itemView.getContext();

            ButterKnife.bind(this, itemView);
        }

        void bind(CoinEntity coin, int position) {
            bindIcon(coin);
            bindSymbol(coin);
            bindPrice(coin);
            bindPercentage(coin);
            bindBackground(position);
        }

        private void bindIcon(CoinEntity coin) {
            Currency currency = Currency.getCurrency(coin.symbol);
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

                symbolText.setText(String.valueOf(coin.symbol.charAt(0)));
            }
        }

        private void bindSymbol(CoinEntity coin) {
            name.setText(coin.symbol);
        }

        private void bindPrice(CoinEntity coin) {
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quote = coin.getQuote(fiat);
            String value = currencyFormatter.format(quote.price, false);

            price.setText(context.getResources().getString(R.string.currency_amount, value, fiat.symbol));
        }

        private void bindPercentage(CoinEntity coin) {
            QuoteEntity quote = coin.getQuote(prefs.getFiatCurrency());
            float percentChangeValue = quote.percentChange24h;
            percentChange.setText(context.getString(R.string.rate_item_percent_change, percentChangeValue));

            if (percentChangeValue >= 0) {
                percentChange.setTextColor(context.getResources().getColor(R.color.percent_change_up));
            } else {
                percentChange.setTextColor(context.getResources().getColor(R.color.percent_change_down));
            }

        }

        private void bindBackground(int position) {
            if (position % 2 == 0) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.rate_item_background_even));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.rate_item_background_odd));
            }
        }
    }
}
