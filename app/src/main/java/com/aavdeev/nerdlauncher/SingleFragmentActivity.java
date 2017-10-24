package com.aavdeev.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    //анотация @LayoutRes сообщает Андройд студио
    //что любая реализация данного метода возвращает
    //действительный индетификатор ресурса макета
    @LayoutRes
    //метод возвращает индетификатор макета
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( getLayoutResId() );

        //добавления фрагмента в активность
        FragmentManager fm = getSupportFragmentManager();
        //Получение экземпляра CrimeFragment по инидификатору контейнерного представления
        Fragment fragment = fm.findFragmentById( R.id.fragment_container );
        //если  fragment уже находиться в списке FragmentManager
        // то вызывается он
        //если нет то создается новый (например поворот экрна)
        if (fragment == null) {
            fragment = createFragment();
            //Создаем и закрепляем транзакцию кода
            fm.beginTransaction()
                    //передаем методу add 2 параметра индетификатор контейнераного фрагменат(где находиться фрагмент)
                    // и  и созданный объект CrimeFragment
                    .add( R.id.fragment_container, fragment )
                    .commit();

        }
    }
}
