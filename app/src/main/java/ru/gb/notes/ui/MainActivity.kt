package ru.gb.notes.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import ru.gb.notes.R
import ru.gb.notes.data.*
import ru.gb.notes.dialog.NoteDialog
import ru.gb.notes.dialog.NoteDialog.NoteDialogController
import ru.gb.notes.dialog.YesNoDialog
import ru.gb.notes.fragment.AboutFragment
import ru.gb.notes.fragment.ListFragment
import ru.gb.notes.fragment.ListFragment.RecyclerController

class MainActivity : AppCompatActivity(), YesNoDialogController, PopupMenuClick, RecyclerController,
    NoteDialogController {
    private val repository = InMemoryRepoImpl.instance
    var listNotes: ListFragment? = null
    private val note: Note? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SharedPref.init(this)
        val savesNotes = SharedPref.read(NOTES_SAVE, null)
        if (savesNotes != null) {
            repository?.readPref(savesNotes)
        }
        repository?.all
        initToolbarAndDrawer()
        val listNoteFragment =
            supportFragmentManager.findFragmentByTag(LIST_FRAGMENT) as ListFragment?
        if (listNoteFragment == null) {
            listNotes = ListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, listNotes!!, LIST_FRAGMENT)
                .commit()
        } else {
            listNotes = supportFragmentManager.findFragmentByTag(LIST_FRAGMENT) as ListFragment?
        }
    }

    private fun initToolbarAndDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        initDrawer(toolbar)
    }

    private fun initDrawer(toolbar: Toolbar) {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view_drawer)
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_drawer_about -> {
                    openAboutFragment()
                    drawer.close()
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_drawer_exit -> {
                    //                    finish();
                    showYesNoDialogFragment()
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun openAboutFragment() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("")
            .add(R.id.fragment_container, AboutFragment(), ABOUT)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_create -> {
                NoteDialog.getInstance(null).show(
                    supportFragmentManager,
                    NoteDialog.NOTE
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun click(command: Int, note: Note?, position: Int) {
        when (command) {
            R.id.context_delete -> {
                if (note != null) {
                    listNotes!!.delete(note, position)
                }
                return
            }
            R.id.context_modify -> NoteDialog.getInstance(note).show(
                supportFragmentManager,
                NoteDialog.NOTE
            )
        }
    }

    override fun update(note: Note?) {
        listNotes!!.update(note)
    }

    override fun create(
        id: Int,
        title: String?,
        description: String?,
        interest: String?,
        dataPerformance: String?
    ) {
        val note =
            Note(id, title.toString(), description.toString(), interest.toString(), dataPerformance)
        listNotes!!.create(note)
    }

    override fun onBackPressed() {
        val aboutFragment = supportFragmentManager.findFragmentByTag(ABOUT) as AboutFragment?
        if (aboutFragment != null) {
            supportFragmentManager.popBackStack()
        } else {
            showYesNoDialogFragment()
        }
    }

    private fun showYesNoDialogFragment() {
        YesNoDialog().show(supportFragmentManager, null)
    }

    override fun createAnswer() {
        finish()
    }

    override fun connect() {
        listNotes!!.passData(note)
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPref.init(applicationContext)
        repository?.writePref(repository.all)
    }

    companion object {
        const val LIST_FRAGMENT = "LIST_FRAGMENT"
        const val ABOUT = "ABOUT"
        const val NOTES_SAVE = "NOTES_SAVE"
    }
}