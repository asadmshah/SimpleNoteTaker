package com.asadmshah.simplenotetaker.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions.OnViewHolderClicked;

public class ColorsListView extends RecyclerView implements OnViewHolderClicked {

    private OnColorSelectedListener onColorSelectedListener;
    private int[] colors;
    private Adapter adapter;

    public ColorsListView(Context context) {
        super(context);
        init();
    }

    public ColorsListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorsListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        colors = getContext().getResources().getIntArray(R.array.tag_colors);
        adapter = new Adapter(colors, this);
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        setAdapter(adapter);
    }

    public void setSelectedColor(@ColorInt int color) {
        adapter.setSelectedColor(color);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        onColorSelectedListener = listener;
    }

    @Override
    public void onViewHolderClicked(ViewHolder viewHolder) {
        if (onColorSelectedListener != null) {
            onColorSelectedListener.onColorSelected(colors[viewHolder.getAdapterPosition()]);
        }
    }

    private static class Adapter extends RecyclerView.Adapter<ColorViewHolder> {

        private final int[] colors;
        private final OnViewHolderClicked onViewHolderClickedListener;
        @ColorInt private int selectedColor = -1;

        public Adapter(int[] colors, OnViewHolderClicked onViewHolderClickedListener) {
            this.colors = colors;
            this.onViewHolderClickedListener = onViewHolderClickedListener;
        }

        public void setSelectedColor(@ColorInt int newColor) {
            int oldColor = selectedColor;
            selectedColor = -1;
            for (int i = 0; i < colors.length; i++) {
                if (colors[i] == oldColor || colors[i] == newColor) {
                    notifyItemChanged(i);
                }
            }
            selectedColor = newColor;
        }

        @Override
        public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int viewSize = parent.getContext().getResources().getDimensionPixelSize(R.dimen.choose_tag_color_view_size);
            int ovalSize = parent.getContext().getResources().getDimensionPixelSize(R.dimen.choose_tag_color_oval_size);

            ImageView view = new ImageView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(viewSize, viewSize));
            view.setScaleType(ImageView.ScaleType.CENTER);
            parent.addView(view);

            ShapeDrawable uncheckedDrawable = new ShapeDrawable(new OvalShape());
            uncheckedDrawable.setIntrinsicWidth(ovalSize);
            uncheckedDrawable.setIntrinsicHeight(ovalSize);
            uncheckedDrawable.getPaint().setColor(Color.WHITE);

            Drawable checkedDrawable = parent.getContext().getDrawable(R.drawable.ic_check_circle_white_24px);
            return new ColorViewHolder(view, uncheckedDrawable, checkedDrawable, onViewHolderClickedListener);
        }

        @Override
        public void onBindViewHolder(ColorViewHolder holder, int position) {
            holder.setChecked(colors[position] == selectedColor);
            holder.setColor(colors[position]);
        }

        @Override
        public int getItemCount() {
            return colors.length;
        }

    }

    private static class ColorViewHolder extends RecyclerView.ViewHolder {

        private final ImageView view;
        private final Drawable uncheckedDrawable;
        private final Drawable checkedDrawable;
        private boolean isChecked = false;

        public ColorViewHolder(View itemView, Drawable uncheckedDrawable, Drawable checkedDrawable, OnViewHolderClicked listener) {
            super(itemView);
            this.uncheckedDrawable = uncheckedDrawable;
            this.checkedDrawable = checkedDrawable;

            view = (ImageView) itemView;
            view.setImageDrawable(uncheckedDrawable);

            itemView.setOnClickListener(v -> listener.onViewHolderClicked(ColorViewHolder.this));
        }

        public void setColor(int color) {
            view.setColorFilter(color);
        }

        public void setChecked(boolean isChecked) {
            if (this.isChecked != isChecked) {
                view.setImageDrawable(isChecked ? checkedDrawable : uncheckedDrawable);
            }
            this.isChecked = isChecked;
        }

    }

    public interface OnColorSelectedListener {
        void onColorSelected(@ColorInt int color);
    }

}
