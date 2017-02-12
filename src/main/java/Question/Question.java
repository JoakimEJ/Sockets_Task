package Question;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Joakim on 25.11.2016.
 */
@DatabaseTable(tableName = "Spørsmål")
public class Question implements Serializable
{
	public static final String ID_FIELD = "ID";

	@DatabaseField(columnName = ID_FIELD, generatedId = true)
	private int ID;

	@DatabaseField(columnName = "Spørsmål")
	private String spm;

	@DatabaseField(columnName = "Svar")
	private String svar;

	Question ()
	{
		// Empty constructor
	}

	public Question (String spm, String svar)
	{
		this.spm = spm;
		this.svar = svar;
	}

	// Getters
	//public int getId() { return ID;	}

	public String getSpm() { return spm; }

	public String getSvar() { return svar; }

	// Setters

	public void setId(int id) { this.ID = id; }
}
