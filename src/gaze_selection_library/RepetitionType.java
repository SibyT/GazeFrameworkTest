package gaze_selection_library;

/**
 * @author Thorsten Bövers
 * A RepetitionType is associated with every TestPlanItem.
 * This represents the ordering in which individual target items are presented to participants during a test.
 * If the number of selections is greater than the number of target items, the ordering starts again at the beginning.
 */
public enum RepetitionType {
	
	/**
	 * If the RANDOM_NO_SUCCESSIVE_REPEATS option is specified, individual target items are randomly selected,
	 * with the restriction that no target item will be selected twice in a row.
	 * This is to avoid a user having to pick the same item twice in succession.
	 */
	RANDOM_NO_SUCCESSIVE_REPEATS,
	
	/**
	 * The RANDOM_NO_REPEATS option also randomizes the targets, with the provision that no target item is
	 * selected more than once, unless all the target items provided by a given GazeTarget have already been selected.
	 * When this occurs, the list of items already selected is cleared and the process starts again.
	 * This ensures that each of the target items is selected.
	 */
	RANDOM_NO_REPEATS,
	
	/**
	 * If the RANDOM option is specified, individual target items are randomly selected with no restriction.
	 */
	RANDOM,
	
	/**
	 * The SEQUENTIAL option selects the target items in a row. Started with the item with smallest indices.
	 */
	SEQUENTIAL,
	
	/**
	 * The REVERSE_SEQUENTIAL option selects the target items in a row. Started with the item with greatest indices.
	 */
	REVERSE_SEQUENTIAL
	
}
