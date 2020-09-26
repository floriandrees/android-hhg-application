package hhg.informatikprojektkurs.design;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hhg.informatikprojektkurs.R;
import hhg.informatikprojektkurs.activity.Settings;
import hhg.informatikprojektkurs.activity.Information;
import hhg.informatikprojektkurs.activity.Homepage;
import hhg.informatikprojektkurs.constants.design.SlideDirection;
import hhg.informatikprojektkurs.handler.files.FileHandler;
import hhg.informatikprojektkurs.handler.surface.NavigationHandler;

public class SlideAdapter extends PagerAdapter {

    private Activity activity;

    private List<String> headerList = null;
    private List<String> contentList = null;

    public SlideAdapter(Activity activity) {
        this.activity = activity;

        initLists();
    }

    private void initLists() {
        List<List<String>> lists = FileHandler.getContentFromAssetsFile(activity, "slide_content.txt");
        headerList = lists.get(0);
        contentList = lists.get(1);
    }

    @Override
    public int getCount() {
        return headerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = activity.getLayoutInflater().inflate(R.layout.homepage_design_slide_adapter, container, false);

        TextView header = view.findViewById(R.id.header);
        TextView content = view.findViewById(R.id.content);

        header.setText(Html.fromHtml(headerList.get(position)));
        content.setText(Html.fromHtml(contentList.get(position)));

        Typeface typeface = Homepage.TYPEFACE_HANDLER.getCurrentTypeface();
        header.setTypeface(typeface);
        content.setTypeface(typeface);

        header.setTextColor(activity.getResources().getColor(R.color.bootstrap_EMERALD));
        container.addView(view);

        if((position == 1) | (position == 3)) {
            Button btn = view.findViewById(R.id.button);
            GradientDrawable drawable = (GradientDrawable) btn.getBackground();
            drawable.setStroke(4, activity.getResources().getColor(R.color.bootstrap_EMERALD));
            btn.setTypeface(typeface);
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 1:
                            NavigationHandler.navigate(activity, Settings.class, null, SlideDirection.LEFT);
                            break;
                        case 3:
                            NavigationHandler.navigate(activity, Information.class, null, SlideDirection.LEFT);
                            break;
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
