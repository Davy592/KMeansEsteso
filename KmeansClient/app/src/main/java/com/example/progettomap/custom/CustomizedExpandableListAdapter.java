package com.example.progettomap.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.progettomap.R;

import java.util.HashMap;
import java.util.List;

/**
 * <h2>La classe CustomizedExpandableListAdapter viene utilizzata per personalizzare l'ExpandableListAdapter</h2>
 * Viene utilizzata per visualizzare l'elenco nell'ExpandableListView
 * L'elenco e' diviso in categorie
 * Le categorie sono le chiavi di una HashMap
 * I valori dell'HashMap sono l'elenco dei valori della categoria
 */
public class CustomizedExpandableListAdapter extends BaseExpandableListAdapter {

    /**
     * Context dell'activity
     */
    private final Context context;
    /**
     * Lista delle categorie
     */
    private final List<String> expandableTitleList;
    /**
     * Lista dei valori delle categorie
     */
    private final HashMap<String, List<String>> expandableDetailList;

    /**
     * <h4>Costruttore</h4>
     *
     * @param context              Context dell'activity
     * @param expandableListTitle  Lista delle categorie
     * @param expandableListDetail Lista dei valori delle categorie
     */
    public CustomizedExpandableListAdapter(Context context, List<String> expandableListTitle,
                                           HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableTitleList = expandableListTitle;
        this.expandableDetailList = expandableListDetail;
    }

    /**
     * <h4>Permette di ottenere il valore di un elemento dell'elenco</h4>
     *
     * @param lstPosn               Posizione dell'elemento nell'elenco
     * @param expanded_ListPosition Posizione dell'elemento nell'elenco espanso
     * @return Valore dell'elemento
     */
    @Override
    public Object getChild(int lstPosn, int expanded_ListPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(lstPosn)).get(expanded_ListPosition);
    }

    /**
     * <h4> Permette di ottenere l'ID di un elemento dell'elenco</h4>
     *
     * @param listPosition          Posizione dell'elemento nell'elenco
     * @param expanded_ListPosition Posizione dell'elemento nell'elenco espanso
     * @return ID dell'elemento
     */
    @Override
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    /**
     * <h4>Restituisce la View (UI) di un elemento dell'elenco</h4>
     *
     * @param lstPosn               Posizione dell'elemento nell'elenco
     * @param expanded_ListPosition Posizione dell'elemento nell'elenco espanso
     * @param isLastChild           Indica se l'elemento e' l'ultimo dell'elenco
     * @param convertView           View dell'elemento
     * @param parent                View del parent
     * @return la child view
     */
    @Override
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(lstPosn, expanded_ListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    /**
     * <h4>Restituisce il numero di elementi dell'elenco</h4>
     *
     * @param listPosition Posizione dell'elemento nell'elenco
     * @return Numero di elementi dell'elenco
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(listPosition)).size();
    }

    /**
     * <h4>Restituisce l'elemento dell'elenco di una data posizione</h4>
     *
     * @param listPosition Posizione dell'elemento nell'elenco
     * @return Elemento dell'elenco
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.expandableTitleList.get(listPosition);
    }

    /**
     * <h4> Permette di ottenere il numero di gruppi</h4>
     *
     * @return Numero di gruppi
     */
    @Override
    public int getGroupCount() {
        return this.expandableTitleList.size();
    }

    /**
     * <h4> Permette di ottenere l'ID di un gruppo</h4>
     *
     * @param listPosition Posizione dell'elemento nell'elenco
     * @return ID dell'elemento
     */
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    /**
     * <h4> Metodo per ottenere la View di un gruppo</h4>
     *
     * @param listPosition Posizione dell'elemento nell'elenco
     * @param isExpanded   Indica se l'elemento e' espanso
     * @param convertView  View dell'elemento
     * @param parent       View del parent
     * @return View dell'elemento
     */
    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    /**
     * <h4> Metodo per indicare se i gruppi hanno ID stabili</h4>
     *
     * @return Indica se i gruppi hanno ID stabili
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * <h4> Metodo per indicare se i gruppi sono selezionabili</h4>
     *
     * @param listPosition Posizione dell'elemento nell'elenco
     * @return Indica se i gruppi sono selezionabili
     */
    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
