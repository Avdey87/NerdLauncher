package com.aavdeev.nerdlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NerdLauncherFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private static final String TAG = "NerdLauncherFragment";

    //Создаем новый newInstance который возращает NerdLauncherFragment
    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    //Метод создания вью
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //передаем в View переменную лайаут fragment_nerd_launcher
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        //в mRecyclerView записываем RecyclerView список разметко данной вью берется из
        //fragment_nerd_launcher_recycler_view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        //mRecyclerView устанавливаем в LayoutManager менеджер макета текущую активити
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        //возвращаем вьюшку
        return view;
    }


    //адаптер
    private void setupAdapter() {

        //создаем новый интент который просматривает манифесты с экшеном ACTION_MAIN
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        //и добавляем в данный интенет еще и категорию
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //Создаем PackageManager переменную в которую записываем текущую активити
        // полученную из пакет менеджера
        final PackageManager pm = getActivity().getPackageManager();
        //создаем список актиностей (запущеных)
        //ResolveInfo содержит методанные о приложении
        List<ResolveInfo> activities = pm.queryIntentActivities
                (startupIntent, 0);
       //выполняем сортировку полученного списка
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString());
            }
        });

//ввыводим лог файл в который пишем сколько найдена активностей(activities.size())
        //и имя активностей
        Log.i(TAG, "Found " + activities.size() + " activities.");
        mRecyclerView.setAdapter(new ActitvityAdapter(activities));
    }

    private class ActivityHolder extends RecyclerView.ViewHolder {
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView;
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);
        }
    }

    private class ActitvityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        public ActitvityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }


        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }




}
