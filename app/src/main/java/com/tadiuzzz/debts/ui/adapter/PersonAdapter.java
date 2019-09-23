package com.tadiuzzz.debts.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.databinding.PersonItemBinding;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.adapter.viewholder.PersonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private OnPersonClickListener onPersonClickListener;
    private List<Person> persons = new ArrayList<>();

    private ObservableField<Boolean> isDataEmpty = new ObservableField<>();

    public ObservableField<Boolean> getIsDataEmpty() {
        return isDataEmpty;
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener){
        this.onPersonClickListener = onPersonClickListener;
    }

    public void setData(List<Person> persons) {
        this.persons.clear();
        this.persons.addAll(persons);
        isDataEmpty.set(persons.isEmpty());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PersonItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.person_item, parent, false);
        binding.setListener(onPersonClickListener);

        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.bind(persons.get(position));
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

}
