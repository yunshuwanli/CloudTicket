package yswl.com.klibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import yswl.com.klibrary.inteface.EventBusSetting;
import yswl.com.klibrary.inteface.FragmentHasMenuSetting;

public class MFragment extends BaseFragment implements EventBusSetting, FragmentHasMenuSetting {

    protected Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onStart() {
        if (getEventBusSetting()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (getEventBusSetting()) {
            if (EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    public MActivity getMActivity() {
        return (MActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getFragmentMenuSetting()) {
            //必须在oncreate之后调用
            setHasOptionsMenu(true);
        }
    }


    @Override
    public boolean getEventBusSetting() {
        return false;
    }

    @Override
    public boolean getFragmentMenuSetting() {
        return false;
    }
}
