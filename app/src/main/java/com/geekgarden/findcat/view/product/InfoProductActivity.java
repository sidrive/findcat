package com.geekgarden.findcat.view.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.utils.ActivityUtils;
import com.geekgarden.findcat.widget.NonScrollableLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class InfoProductActivity extends AppCompatActivity {

    private List<Product> products;
    private InfoProductAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_product);

        init();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void loadData() {
        products.clear();
        products.add(new Product("Cat Motif Batu Alam", "adalah cat tekstur khusus yang dapat menciptakan motif alami khas batu-batuan yang diaplikasikan dengan metode semprot (spray gun khusus cat motif batu alam dengan kompresor berkekuatan kurang lebih 1HP). Cat ini ramah lingkungan dan tidak mengandung racun. Warna tidak mudah pudar dan tidak mudah rontok. Cat Motif Batu Alam WAWAWA dapat digunakan untuk menutup bagian tembok yang kurang sempurna serta dapat digunakan untuk menciptakan desain unik khas batu alam.", R.drawable.sample_motif_batu_alam));
        products.add(new Product("Mesin Roll Motif", "adalah tempat roll motif yang digunakan untuk memudahkan dalam membentuk motif tipe flat di dinding, Mesin roll motif ini dapat digunakan berkali-kali dengan motif yang berbeda-beda sesuai dengan ukurannya (inch).", R.drawable.sample_roll_motif));
        products.add(new Product("Bulu Roll Motif", "adalah alat untuk mengecat seperti halnya kuas atau roll biasa, perbedaan pada bulu roll motif ini yaitu bisa membentuk gambar sesuai dengan motifnya, dengan cara memasukkan bulu roll motif ini ke mesin roll motif yang telah diisi dengan cat lalu aplikasikan ke media yang kering, maka akan membentuk motif dengan tipe flat/datar. Bulu roll motif ini dapat dipakai berulang-ulang dengan warna-warna kesukaan Anda serta lebih awet karena terbuat dari bahan karet.", R.drawable.sample_roll_motif_bulu));
        products.add(new Product("Roll Motif Dengan Gagang", "adalah alat untuk mengecat seperti halnya kuas atau roll biasa, perbedaan pada roll motif dengan gagang ini yaitu bisa membentuk gambar sesuai dengan motifnya, dengan cara membasahi roll motif dengan air biasa lalu aplikasikan ke media yang masih basah atau baru di cat, maka akan membentuk motif dengan tipe timbul tenggelam. Roll Motif ini dapat dipakai berulang-ulang dan lebih awet karena terbuat dari bahan karet.", R.drawable.sample_roll_motif_gagang));
        products.add(new Product("Envi", "adalah cat tembok yang memberikan permukaan yang halus dan mudah dibersihkan dengan air. Envi Latex Wall Paint dapat diaplikasikan untuk segala jenis permukaan tembok dan plafon yang terbuat dari beton, plaster, fiber, triplex maupun kayu.", R.drawable.sample_envi));
        products.add(new Product("Electric Spray Gun Joust Max", "adalah peralatan pertukangan tambahan yang penggunaanya dengan cara disemprot. Penggunaan Electric Spray Gun Joust Max tidak perlu menggunakan kompresor. Mudah digunakan, hanya perlu menghubungkan peralatan dengan listrik.", R.drawable.sample_electric_spray));

        adapter.notifyDataSetChanged();
    }

    private void init() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        products = new ArrayList<>();
        adapter = new InfoProductAdapter(this, products, onAdapterListener);
        ((RecyclerView) findViewById(R.id.recycler_video)).setLayoutManager(new NonScrollableLinearLayoutManager(this));
        ((RecyclerView) findViewById(R.id.recycler_video)).setHasFixedSize(true);
        ((RecyclerView) findViewById(R.id.recycler_video)).setAdapter(adapter);
    }

    private InfoProductAdapter.OnAdapterListener onAdapterListener = position -> {
        Product product = products.get(position);

        SingleProductActivity.Param param = new SingleProductActivity.Param();
        param.product = product;

        ActivityUtils.startActivityWParam(this, SingleProductActivity.class, SingleProductActivity.paramKey, param);
    };
}
