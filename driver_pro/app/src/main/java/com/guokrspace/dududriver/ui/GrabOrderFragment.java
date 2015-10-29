package com.guokrspace.dududriver.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guokrspace.dududriver.R;
import com.guokrspace.dududriver.adapter.OrderListAdapter;
import com.guokrspace.dududriver.model.OrderListItem;
import com.guokrspace.dududriver.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyman on 15/10/22.
 */
public class GrabOrderFragment extends BaseFragment {

    @Bind(R.id.date_tv)
    TextView tvDate;
    @Bind(R.id.ordernum_tv)
    TextView tvOrderNum;
    @Bind(R.id.onlinetime_tv)
    TextView tvOnlineTime;
    @Bind(R.id.income_tv)
    TextView tvIncome;
    @Bind(R.id.turnover_rate_tv)
    TextView tvTurnoverRate;
    @Bind(R.id.graborder_rv)
    RecyclerView mRecyclerView;
    private Context context;

    private OrderListAdapter mAdapter;

    public static GrabOrderFragment newInstance() {
        final GrabOrderFragment grabOrderFragment = new GrabOrderFragment();
//        final Bundle args = new Bundle();
        return grabOrderFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graborder, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mAdapter = new OrderListAdapter(initData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<OrderListItem> initData() {
        List<OrderListItem> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new OrderListItem("10-09 15:52", "发现订单密集区域，点击查看~" + i));
        }
        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
