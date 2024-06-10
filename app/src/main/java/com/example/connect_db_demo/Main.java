package com.example.connect_db_demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<Food> foodList;
    private FloatingActionButton fab;
    private DatabaseHelper db;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_db);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        db = new DatabaseHelper();

        loadFoodList();

        fab.setOnClickListener(v -> showAddFoodDialog());
    }

    private void loadFoodList() {
        executorService.execute(() -> {
            foodList = db.getAllFoods();
            runOnUiThread(() -> {
                adapter = new FoodAdapter(foodList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                    @Override
                    public void onEditClick(int position) {
                        showEditFoodDialog(position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        deleteFood(position);
                    }
                });
            });
        });
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Food");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_food, null);
        final EditText etName = view.findViewById(R.id.etName);
        builder.setView(view);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etName.getText().toString();
            if (!name.isEmpty()) {
                addFood(name);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void showEditFoodDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Food");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_food, null);
        final EditText etName = view.findViewById(R.id.etName);
        etName.setText(foodList.get(position).getName());
        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString();
            if (!name.isEmpty()) {
                updateFood(foodList.get(position).getId(), name, position);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void addFood(String name) {
        executorService.execute(() -> {
            boolean result = db.addFood(name);
            if (result) {
                runOnUiThread(() -> {
                    Food newFood = new Food(foodList.size() + 1, name); // Assuming id starts from 1 and increments by 1
                    foodList.add(newFood);
                    // Notify the adapter about the data change
                    adapter.notifyItemInserted(foodList.size() - 1);
                });
            }
        });
    }

    private void updateFood(int id, String name, int position) {
        executorService.execute(() -> {
            boolean result = db.updateFood(id, name);
            if (result) {
                runOnUiThread(() -> {
                    foodList.get(position).setName(name);
                    adapter.notifyItemChanged(position);
                });
            }
        });
    }

    private void deleteFood(int position) {
        executorService.execute(() -> {
            boolean result = db.deleteFood(foodList.get(position).getId());
            if (result) {
                runOnUiThread(() -> {
                    foodList.remove(position);
                    adapter.notifyItemRemoved(position);
                });
            }
        });
    }
}
