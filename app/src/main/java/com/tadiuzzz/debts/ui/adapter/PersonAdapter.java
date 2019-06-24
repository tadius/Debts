package com.tadiuzzz.debts.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Person;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private OnPersonClickListener onPersonClickListener;
    private List<Person> persons;

    public void setData(List<Person> persons) {
        this.persons = persons;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View personView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);

        return new PersonViewHolder(personView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = persons.get(position);

        holder.tvPersonItemName.setText(person.getName());
    }

    @Override
    public int getItemCount() {
        return persons == null ? 0 : persons.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPersonItemName) TextView tvPersonItemName;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(onPersonClickListener != null && position != RecyclerView.NO_POSITION) {
                    onPersonClickListener.onPersonClick(persons.get(position));
                }
            });
        }
    }

    public interface OnPersonClickListener{
        void onPersonClick(Person person);
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener){
        this.onPersonClickListener = onPersonClickListener;
    }

}
