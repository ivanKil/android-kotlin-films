package com.lessons.films.view.contacts

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lessons.films.R
import com.lessons.films.databinding.FilmsLentaBinding
import com.lessons.films.model.AppState
import com.lessons.films.model.FilmContact
import com.lessons.films.view.MainViewModel


const val REQUEST_CODE_CONTACTS = 42
const val REQUEST_CODE_CALL = 43

open class ContactsFragment : Fragment() {
    private val contactsAdapter: ContactsAdapter by lazy { ContactsAdapter() }
    var binding: FilmsLentaBinding? = null
    protected val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    protected var appState: AppState? = null
    lateinit private var lastNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FilmsLentaBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    open fun setTitle() = binding!!.tvTitleList.setText(R.string.contacts)

    open fun requestData() = viewModel.requestNowPlayingFilms()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appState ?: requestData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filmsList: RecyclerView = view.findViewById(R.id.list_layot)
        setTitle()
        filmsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        contactsAdapter.clickListener = object : OnContactClicked {
            override fun onContactClicked(film: FilmContact) = actOnClick(film)
        }

        filmsList.adapter = contactsAdapter
        checkPermission()
    }

    private fun actOnClick(contact: FilmContact) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.chose_phone)
        builder.setItems(contact.phones.toTypedArray()) { _, ind ->
            lastNumber = contact.phones[ind]
            checkPermissionCall()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun callByNumber() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:" + lastNumber)
        startActivity(intent)
    }

    private fun checkPermissionCall() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    callByNumber()
                }
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_CALL)
                }
            }
        }
    }

    // Проверяем, разрешено ли чтение контактов
    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    contactsAdapter.setData(viewModel.getContacts())
                }
                //Опционально: если нужно пояснение перед запросом разрешений
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                            .setTitle(R.string.contact_access)
                            .setMessage(R.string.contact_access_reason)
                            .setPositiveButton(R.string.yes) { _, _ ->
                                requestPermissionContacts()
                            }
                            .setNegativeButton(R.string.no) { dialog, _ -> onNoAccess(dialog) }
                            .create()
                            .show()
                }
                else -> {
                    //Запрашиваем разрешение
                    requestPermissionContacts()
                }
            }
        }
    }

    private fun requestPermissionContacts() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_CONTACTS)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_CONTACTS -> {
                // Проверяем, дано ли пользователем разрешение по нашему запросу
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    contactsAdapter.setData(viewModel.getContacts())
                } else {
                    // Поясните пользователю, что экран останется пустым, потому что доступ к контактам не предоставлен
                    context?.let {
                        AlertDialog.Builder(it)
                                .setTitle(R.string.contact_access).setMessage(R.string.contact_access_explaning)
                                .setNegativeButton(R.string.close) { dialog, _ -> onNoAccess(dialog) }
                                .create().show()
                    }
                }
                return
            }
            REQUEST_CODE_CALL -> {
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    callByNumber()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                                .setTitle(R.string.call_refused).setMessage(R.string.call_refused_text)
                                .setNegativeButton(R.string.close) { dialog, _ -> dialog.dismiss() }
                                .create().show()
                    }
                }
            }
        }
    }

    private fun onNoAccess(dialogInterface: DialogInterface) {
        dialogInterface.dismiss()
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.navigation_films)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        contactsAdapter.removeListener()
        super.onDestroy()
    }
}
