package Question;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joakim on 28.11.2016.
 * This class contains all the questions used in this program. It has only one method, populateQuestionTable,
 * which populates the database table with the questions below.
 */
public class QuestionManager
{
	public void populateQuestionTable (Dao<Question, Integer> questionDao)
	{
		List<Question> questionList = new ArrayList<>();

		Question spm1 = new Question(
				"Stevie Wonder and Paul Mccartney sang together in which song?",
				"Ebony and Ivory");
		questionList.add(spm1);

		Question spm2 = new Question(
				"Who left boy band 'Take That' in July 1995?",
				"Robbie Williams");
		questionList.add(spm2);

		Question spm3 = new Question(
				"Which girls name featured in a top ten hit for David Bowie in 1984 " +
						"and a number one for Michael Jackson in 1983?",
				"Jean");
		questionList.add(spm3);

		Question spm4 = new Question(
				"Who has a hit with I’m too sexy in 1991?",
				"Right Said Fred");
		questionList.add(spm4);

		Question spm5 = new Question(
				"Meatloaf had only 1 UK number one. Name the song.",
				"I'd Do Anything For Love");
		questionList.add(spm5);

		Question spm6 = new Question(
				"Which famous musician is supposed to have said 'We're more popular than Jesus now'?",
				"John Lennon");
		questionList.add(spm6);

		Question spm7 = new Question(
				"Which rock group provided the soundtrack music for the film 'Flash Gordon'?",
				"Queen");
		questionList.add(spm7);

		Question spm8 = new Question(
				"What nationality were the rock band Thin Lizzy?",
				"Irish");
		questionList.add(spm8);

		Question spm9 = new Question(
				"Who is noted for his guitar solo on Stairway to Heaven?",
				"Jimmy Page");
		questionList.add(spm9);

		Question spm10 = new Question(
				"In 2013, who resumed his career with his third and fourth albums The 20/20 Experience" +
						" and The 20/20 Experience – 2 of 2, exploring new soul styles with " +
						"1960s and 1970s rock?",
				"Justin Timberlake");
		questionList.add(spm10);

		try
		{
			int idCounter = 1;
			// Send questions to the database - or, as the example states, PERSIST the objects to the database!
			for (Question q : questionList)
			{
				q.setId(idCounter++);
				questionDao.createIfNotExists(q);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
