package trova.funghi.view.mushroom;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import trova.funghi.R;
import trova.funghi.adapter.MushroomListAdapter;
import trova.funghi.persistence.entity.Mushroom;

public class MushroomCatalogueView extends LinearLayout {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    // CONTEXT
    protected Context context;

    // MODEL
    private List<Mushroom> mushroomList;

    // VIEWS
    android.support.v7.widget.RecyclerView catalogueRecyclerView;

    public MushroomCatalogueView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MushroomCatalogueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MushroomCatalogueView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;

        inflate(context, R.layout.mushroom_catalogue_layout, this);

        // catalogueListView = (ListView) findViewById(R.id.catalogue_list);

        // MushroomListAdapter adapter = new MushroomListAdapter(context, R.layout.mushroom_list_item_layout, mushroomList);

        // catalogueListView.setAdapter(adapter);
    }

    public void setMushroomList(List<Mushroom> mushroomList) {
        this.mushroomList = mushroomList;
    }
}
