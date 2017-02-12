package DBFunctionality;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Joakim on 08.12.2016.
 */
@DatabaseTable(tableName = "Highscore")
public class HighScore implements Serializable
{
	private static final String ID_FIELD = "ID";
	private static final String NAME_FIELD = "Player";
	private static final String SCORE_FIELD = "Score";

	@DatabaseField(columnName = ID_FIELD, generatedId = true)
	private int ID;

	@DatabaseField(columnName = NAME_FIELD)
	private String name;

	@DatabaseField(columnName = SCORE_FIELD)
	private double score;

	// Constructors
	public HighScore() {}

	// Getters
	public double getScore() { return this.score; }

	// Setters
	public void setScore(double score) { this.score = score;  }
	public void setName(String name) { this.name = name; }

	@Override
	public String toString()
	{
		return this.name + " with Score: " + this.score;
	}
}
