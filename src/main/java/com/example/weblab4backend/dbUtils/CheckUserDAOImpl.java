package com.example.weblab4backend.dbUtils;

import com.example.weblab4backend.beans.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class CheckUserDAOImpl implements CheckUserDAO {
    private static final String PERSISTENCE_UNIT_NAME = "default";
    private final EntityManagerFactory emf;

    public CheckUserDAOImpl() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }


    @Override
    public void addUser(User user) throws SQLException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("DAO error occurred: " + e);
            throw new SQLException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void updateUser(Long user_id, User user) throws SQLException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("DAO error occurred: " + e);
            throw new SQLException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User getUserById(Long user_id) throws SQLException {
        EntityManager em = emf.createEntityManager();
        User foundUser;

        try {
            foundUser = em.find(User.class, user_id);
        } catch (Exception e) {
            System.err.println("DAO error occurred: " + e);
            throw new SQLException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return foundUser;
    }

    @Override
    public Collection<User> getAllUsers() throws SQLException {
        EntityManager em = emf.createEntityManager();
        List<User> foundedUsers;

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            foundedUsers = em.createQuery(criteriaQuery.select(root)).getResultList();
        } catch (Exception e) {
            System.err.println("DAO error occurred: " + e);
            throw new SQLException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return foundedUsers;
    }

    @Override
    public void deleteUser(User user) throws SQLException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.remove(em.contains(user) ? user : em.merge(user));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("DAO error occurred: " + e);
            throw new SQLException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        EntityManager em = emf.createEntityManager();
        User foundUser;

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root).where(cb.equal(root.get("username"), username));
            foundUser = em.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println("DAO error occurred: " + e);
            throw new SQLException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return foundUser;
    }
}
