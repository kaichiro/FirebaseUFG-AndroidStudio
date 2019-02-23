package com.example.firebaseufg;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final String TABLE_PESSOA = "Pessoa";

    EditText nome, apelido, email, senha;
    ListView list_pessoas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Pessoa> listPessoas = new ArrayList<>();
    ArrayAdapter<Pessoa> arrayAdapterPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.txtNome);
        apelido = findViewById(R.id.txtApelido);
        email = findViewById(R.id.txtEMail);
        senha = findViewById(R.id.txtSenha);

        list_pessoas = findViewById(R.id.lvPessoas);

        inicializaFirebase();

        listarDados();
    }

    private void listarDados() {
        databaseReference.child(TABLE_PESSOA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot obsSnapshot : dataSnapshot.getChildren()) {
                    Pessoa p = obsSnapshot.getValue(Pessoa.class);
                    listPessoas.add(p);

                    arrayAdapterPessoa = new ArrayAdapter<Pessoa>(MainActivity.this, android.R.layout.activity_list_item);
                    list_pessoas.setAdapter(arrayAdapterPessoa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializaFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nome_ = nome.getText().toString();
        String apelido_ = apelido.getText().toString();
        String email_ = email.getText().toString();
        String senha_ = senha.getText().toString();

        switch (item.getItemId()) {
            case R.id.icon_add:
                if (("").equals(nome_)) {
                    valida();
                } else {
                    Pessoa p = new Pessoa(UUID.randomUUID().toString(), nome_, apelido_, email_, senha_);
                    databaseReference.child(TABLE_PESSOA).child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Registro gerado", Toast.LENGTH_LONG).show();
                    limparCampos();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void limparCampos() {
        nome.setText("");
        apelido.setText("");
        email.setText("");
        senha.setText("");
    }

    private void valida() {
        String nome_ = nome.getText().toString();
        String apelido_ = apelido.getText().toString();
        String email_ = email.getText().toString();
        String senha_ = senha.getText().toString();

        if (nome_.equals(""))
            nome.setError("campo requerido");
        if (apelido_.equals(""))
            apelido.setError("campo requerido");
        if (email_.equals(""))
            email.setError("campo requerido");
        if (senha_.equals(""))
            senha.setError("campo requerido");
    }
}
