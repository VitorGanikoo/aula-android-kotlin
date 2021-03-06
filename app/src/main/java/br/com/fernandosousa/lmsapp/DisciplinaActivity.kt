package br.com.fernandosousa.lmsapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

class DisciplinaActivity : DebugActivity() {

    private val context: Context get() = this
    var disciplina: Disciplina? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplina)

        // recuperar onjeto de Disciplina da Intent
        disciplina = intent.getSerializableExtra("disciplina") as Disciplina

        // configurar título com nome da Disciplina e botão de voltar da Toobar
        // colocar toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // alterar título da ActionBar
        supportActionBar?.title = disciplina?.nome

        // up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // atualizar dados do carro
        var texto = findViewById<TextView>(R.id.nomeDisciplina)
        texto.text = disciplina?.nome
        var imagem = findViewById<ImageView>(R.id.imagemDisciplina)
        Picasso.with(this).load(disciplina?.foto).fit().into(imagem,
                object: com.squareup.picasso.Callback{
                    override fun onSuccess() {}

                    override fun onError() { }
                })
    }

    // método sobrescrito para inflar o menu na Actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // infla o menu com os botões da ActionBar
        menuInflater.inflate(R.menu.menu_main_disciplina, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // id do item clicado
        val id = item?.itemId
        // verificar qual item foi clicado
        // remover a disciplina no WS
        if  (id == R.id.action_remover) {
            // alerta para confirmar a remeção
            // só remove se houver confirmação positiva
            AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Deseja excluir a disciplina")
                    .setPositiveButton("Sim") {
                        dialog, which ->
                            dialog.dismiss()
                            taskExcluir()
                    }.setNegativeButton("Não") {
                        dialog, which -> dialog.dismiss()
                    }.create().show()
        }
        // botão up navigation
        else if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun taskExcluir() {
        if (this.disciplina != null && this.disciplina is Disciplina) {
            // Thread para remover a disciplina
            Thread {
                DisciplinaService.delete(this.disciplina as Disciplina)
                runOnUiThread {
                    // após remover, voltar para activity anterior
                    finish()
                }
            }.start()
        }
    }

}
