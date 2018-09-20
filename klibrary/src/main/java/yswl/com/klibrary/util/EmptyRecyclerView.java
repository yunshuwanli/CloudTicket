package yswl.com.klibrary.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import yswl.com.klibrary.R;


public class EmptyRecyclerView extends FrameLayout implements View.OnClickListener {
    public EmptyRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    TextView noticeTV;
    TextView contentTV;
    ImageView noticeIM;

    private EmptyViewOnClickLister mEmptyViewOnClickLister;

    public void setmEmptyViewOnClickLister(
            EmptyViewOnClickLister mEmptyViewOnClickLister) {
        this.mEmptyViewOnClickLister = mEmptyViewOnClickLister;
    }

    public interface EmptyViewOnClickLister {
        void onEmptyViewClick(View v);
    }

    private void init(Context context) {
        View ui = LayoutInflater.from(context).inflate(
                R.layout.common_recycler_emptyview, null);
        noticeTV = (TextView) ui.findViewById(R.id.recycler_empty_view_notice);
        contentTV = (TextView) ui.findViewById(R.id.recycler_empty_view_desc);
        noticeIM = (AppCompatImageView) ui.findViewById(R.id.recycler_empty_view_icon);
        this.addView(ui);
        this.setOnClickListener(this);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置占位图片与文字，null为不显示
     *
     * @param notice  提示文字
     * @param content 描述文字
     * @param icon    图片资源id
     */
    public void setNoticeAndIcon(String notice, String content, int icon) {
        MTextViewUtil.setText(noticeTV, notice);
        MTextViewUtil.setText(contentTV, content);
        if (-1 != icon && noticeIM != null) {
            noticeIM.setImageResource(icon);
        }
    }

    @Override
    public void onClick(View v) {
        if (mEmptyViewOnClickLister != null) {
            mEmptyViewOnClickLister.onEmptyViewClick(v);
        }
    }
}
