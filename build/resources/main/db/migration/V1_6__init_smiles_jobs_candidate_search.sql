CREATE TABLE candidate_search (
    candidate_search_id SERIAL NOT NULL,
    search_criteria TEXT,
    search_results TEXT,
    PRIMARY KEY (candidate_search_id)
);

