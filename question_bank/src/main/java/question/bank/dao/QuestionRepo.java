package question.bank.dao;

import org.springframework.data.couchbase.repository.CouchbaseRepository;

import question.bank.model.QuestionModel;

public interface QuestionRepo extends CouchbaseRepository<QuestionModel, String> {

}
