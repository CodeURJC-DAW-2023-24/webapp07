package com.daw.webapp07.repository;

import com.daw.webapp07.model.Project;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerName(String ownerName);

    @Query(value="""
WITH UCategoryInvestment AS (
		 SELECT
			 ua.id AS user_entity_id,
			 p.category AS category,
			 SUM(i.amount) AS total_investment
		 FROM
			 user_entity ua
		 JOIN Inversion i ON i.user_id = ua.id
		 JOIN Project p ON i.project_id = p.id
		 GROUP BY
			 ua.id, p.category),
UCategoryPercentage AS (
		SELECT 
			uci.user_entity_id,
			uci.category,
			uci.total_investment / u.total_investment AS percentage
		FROM
			UCategoryInvestment uci
				JOIN
			(SELECT 
				user_entity_id, SUM(total_investment) AS total_investment
			FROM
				UCategoryInvestment
			GROUP BY user_entity_id) u ON u.user_entity_id = uci.user_entity_id
		GROUP BY uci.user_entity_id , uci.category),
UCategoryDifference AS (
		SELECT 
			u1.user_entity_id AS user1,
			u2.user_entity_id AS user2,
			u1.category,
			ABS(u1.percentage - u2.percentage) AS difference
		FROM
			UCategoryPercentage u1
				JOIN
			UCategoryPercentage u2 ON u1.category = u2.category
				AND u1.user_entity_id != u2.user_entity_id),
 USimilarity  AS (
		SELECT 
			u1.user1, u2.user2, SUM(u1.difference) AS total_difference
		FROM
			UCategoryDifference u1
				JOIN
			UCategoryDifference u2 ON u1.user1 = u2.user1
				AND u1.user2 = u2.user2
		GROUP BY u1.user1 , u2.user2),
 UProjectDonationSum AS(
		SELECT 
			user_id AS user_entity_id,
			project_id AS project,
			SUM(amount) AS total_donation
		FROM
			Inversion
		GROUP BY user_id, project_id),
    
 UProjectRelation AS (
    SELECT 
        u.id AS user_entity_id,
        p.id AS projec,
        COALESCE(SUM((us.total_difference * upds.total_donation)) / SUM(upds.total_donation), 200) AS relation_number
    FROM
        user_entity u
        CROSS JOIN Project p
        LEFT JOIN UProjectDonationSum upds ON upds.project = p.id
        LEFT JOIN USimilarity us ON (us.user1 = u.id  AND us.user2 = user_entity_id)
    GROUP BY u.id, p.id)

SELECT 
	p.*
FROM 
	UProjectRelation u, Project p
WHERE 
	u.user_entity_id = ?1 AND
    u.projec = p.id
ORDER BY u.relation_number;
    """,countQuery = """ 
SELECT 
    COUNT(*)
FROM
Project p
""", nativeQuery = true)
    public Page<Project> recomendedProjects(long userid, Pageable pageable);

    public void deleteById(long id);
}
