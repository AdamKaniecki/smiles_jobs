CREATE TABLE company_search (
    company_search_id SERIAL NOT NULL,
    search_criteria TEXT,
    search_results TEXT,
    PRIMARY KEY (company_search_id)
);
