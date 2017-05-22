package com.example;

import android.app.Activity;
import android.view.View;

/**
 * Created by zhy on 16/4/22.
 */
public class ViewInjectUtil
{
    private static final String SUFFIX = "$$InjectAdapter";

    public static void injectView(Activity activity)
    {
        InjectAdapter proxyActivity = findProxyActivity(activity);
        proxyActivity.inject( activity);
    }

//    public static void injectView(View view)
//    {
//        InjectAdapter proxyActivity = findProxyActivity(object);
//        proxyActivity.inject(view);
//    }

    private static InjectAdapter findProxyActivity(Object activity)
    {
        try
        {
            Class clazz = activity.getClass();
            Class injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (InjectAdapter) injectorClazz.newInstance();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
    }
}
