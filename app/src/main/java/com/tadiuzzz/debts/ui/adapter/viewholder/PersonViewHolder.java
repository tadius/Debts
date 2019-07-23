package com.tadiuzzz.debts.ui.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import com.tadiuzzz.debts.databinding.PersonItemBinding;
import com.tadiuzzz.debts.domain.entity.Person;

/**
 * Created by Simonov.vv on 22.07.2019.
 */
public class PersonViewHolder extends RecyclerView.ViewHolder {

    PersonItemBinding binding;

    public PersonViewHolder(PersonItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void  bind(Person person) {
        binding.setPerson(person);
        binding.executePendingBindings();
    }
}

