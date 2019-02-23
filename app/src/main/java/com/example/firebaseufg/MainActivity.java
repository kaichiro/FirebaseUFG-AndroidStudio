package com.example.firebaseufg;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    EditText et_nome, et_apelido, et_email, et_senha;
    ListView listView_pessoas;

    Pessoa pessoaSelecionada;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Pessoa> list_pessoas = new ArrayList<>();
    ArrayAdapter<Pessoa> arrayAdapter_pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nome = findViewById(R.id.txtNome);
        et_apelido = findViewById(R.id.txtApelido);
        et_email = findViewById(R.id.txtEMail);
        et_senha = findViewById(R.id.txtSenha);

        listView_pessoas = findViewById(R.id.lvPessoas);

        inicializaFirebase();

        listarDados();

        listView_pessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoaSelecionada = (Pessoa) parent.getItemAtPosition(position);
                et_nome.setText(pessoaSelecionada.getNome());
                et_apelido.setText(pessoaSelecionada.getApelido());
                et_email.setText(pessoaSelecionada.getEmail());
                et_senha.setText(pessoaSelecionada.getSenha());
            }
        });
    }

    private void listarDados() {
        databaseReference.child(TABLE_PESSOA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_pessoas.clear();

                for (DataSnapshot obsSnapshot : dataSnapshot.getChildren()) {
                    Pessoa p = obsSnapshot.getValue(Pessoa.class);
                    list_pessoas.add(p);

                }
                arrayAdapter_pessoa = new ArrayAdapter<Pessoa>(MainActivity.this, android.R.layout.simple_list_item_1, list_pessoas);
                listView_pessoas.setAdapter(arrayAdapter_pessoa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializaFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.setPersistenceEnabled(true);

        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nome_ = et_nome.getText().toString();
        String apelido_ = et_apelido.getText().toString();
        String email_ = et_email.getText().toString();
        String senha_ = et_senha.getText().toString();


        switch (item.getItemId()) {
            case R.id.icon_add: {
                if (("").equals(nome_)) {
                    valida();
                } else {
                    Pessoa p = new Pessoa(UUID.randomUUID().toString(), nome_, apelido_, email_, senha_);
                    databaseReference.child(TABLE_PESSOA).child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Adicionando registro", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.icon_del: {
                Pessoa p = new Pessoa(pessoaSelecionada.getUid());
                databaseReference.child(TABLE_PESSOA).child(p.getUid()).removeValue();
                Toast.makeText(this, "Registro deletado", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_update: {
                Pessoa p = new Pessoa(
                        pessoaSelecionada.getUid()
                        , et_nome.getText().toString()
                        , et_apelido.getText().toString()
                        , et_email.getText().toString()
                        , et_senha.getText().toString()
                );
                databaseReference.child(TABLE_PESSOA).child(p.getUid()).setValue(p);
                Toast.makeText(this, "Atualizando registro", Toast.LENGTH_LONG).show();

                break;
            }
        }

        limparCampos();
        listarDados();

        return super.onOptionsItemSelected(item);
    }

    private void limparCampos() {
        et_nome.setText("");
        et_apelido.setText("");
        et_email.setText("");
        et_senha.setText("");
    }

    private void valida() {
        String nome_ = et_nome.getText().toString();
        String apelido_ = et_apelido.getText().toString();
        String email_ = et_email.getText().toString();
        String senha_ = et_senha.getText().toString();

        if (nome_.equals(""))
            et_nome.setError("campo requerido");
        if (apelido_.equals(""))
            et_apelido.setError("campo requerido");
        if (email_.equals(""))
            et_email.setError("campo requerido");
        if (senha_.equals(""))
            et_senha.setError("campo requerido");
    }
}
