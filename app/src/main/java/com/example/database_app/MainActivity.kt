package com.example.database_app

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var editTextName: EditText
    private lateinit var editTextDeleteID: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listView)
        editTextName = findViewById(R.id.editTextName)
        editTextDeleteID = findViewById(R.id.editTextDeleteID)

        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate) // New Update button
        val buttonDelete: Button = findViewById(R.id.buttonDelete)

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString()
            if (name.isNotEmpty()) {
                if (dbHelper.insertData(name)) {
                    Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show()
                    editTextName.text.clear()
                    loadData()
                } else {
                    Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        buttonUpdate.setOnClickListener {
            val id = editTextDeleteID.text.toString()
            val newName = editTextName.text.toString()
            if (id.isNotEmpty() && newName.isNotEmpty()) {
                if (dbHelper.updateData(id, newName)) {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    editTextName.text.clear()
                    editTextDeleteID.text.clear()
                    loadData()
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter both ID and new name", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelete.setOnClickListener {
            val id = editTextDeleteID.text.toString()
            if (id.isNotEmpty()) {
                if (dbHelper.deleteData(id)) {
                    Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    editTextDeleteID.text.clear()
                    loadData()
                } else {
                    Toast.makeText(this, "Deletion Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter an ID", Toast.LENGTH_SHORT).show()
            }
        }

        loadData()
    }

    private fun loadData() {
        val data: Cursor = dbHelper.getData()
        val list = ArrayList<String>()

        if (data.moveToFirst()) {
            do {
                val id = data.getInt(data.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val name = data.getString(data.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                list.add("ID: $id, Name: $name")
            } while (data.moveToNext())
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }
}
