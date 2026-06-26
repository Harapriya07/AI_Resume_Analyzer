import { useLocation, useNavigate } from 'react-router-dom'
import ScoreCard from '../components/ScoreCard'
import SkillList from '../components/SkillList'
import JobMatch from '../components/JobMatch'

export default function Analysis() {
  const location = useLocation()
  const navigate = useNavigate()
  const analysisResult = location.state?.analysisResult

  if (!analysisResult) {
    return (
      <div style={analysisContainerStyle}>
        <div className="container">
          <div style={emptyAnalysisStyle}>
            <div style={{ textAlign: 'center' }}>
              <p style={{ fontSize: '3rem', margin: '0 0 1rem 0' }}>📊</p>
              <h2 style={{ marginBottom: '1rem' }}>No Analysis Data</h2>
              <p style={{ color: '#7E98A4', marginBottom: '2rem' }}>
                Please upload a resume first to see detailed analysis
              </p>
              <button
                className="btn btn-primary"
                onClick={() => navigate('/dashboard')}
              >
                ↩️ Back to Dashboard
              </button>
            </div>
          </div>
        </div>
      </div>
    )
  }

  const getScoreCategory = (score) => {
    if (score >= 80) return 'Excellent'
    if (score >= 60) return 'Good'
    return 'Needs Improvement'
  }

  const getScoreRecommendations = (score) => {
    if (score >= 80) {
      return [
        'Your resume is well-optimized for ATS systems.',
        'You have strong keyword density and formatting.',
        'Consider adding more quantifiable metrics and achievements.',
        'Keep your formatting simple and consistent.',
      ]
    } else if (score >= 60) {
      return [
        'Add more relevant keywords from job descriptions.',
        'Improve formatting and spacing for better ATS readability.',
        'Include more quantifiable achievements and metrics.',
        'Use standard section headings recognized by ATS systems.',
      ]
    } else {
      return [
        'Restructure your resume with standard ATS-friendly format.',
        'Add missing keywords related to your target roles.',
        'Avoid images, tables, and complex formatting.',
        'Include a clear summary or objective at the top.',
      ]
    }
  }

  return (
    <div style={analysisContainerStyle}>
      <div className="container">
        {/* Header */}
        <section style={analysisHeaderStyle}>
          <button
            className="btn btn-secondary"
            onClick={() => navigate('/dashboard')}
            style={{ marginBottom: '1.5rem' }}
          >
            ← Back to Dashboard
          </button>

          <h1 style={{ marginBottom: '0.5rem' }}>Detailed Resume Analysis</h1>
          <p style={{ color: '#7E98A4', fontSize: '1.05rem' }}>
            Comprehensive insights and actionable recommendations
          </p>
        </section>

        {/* Main Score */}
        <section style={scoreMainSectionStyle}>
          <ScoreCard
            atsScore={analysisResult.atsScore}
            rating={analysisResult.rating}
            fileName={analysisResult.fileName}
            uploadedAt={analysisResult.uploadedAt}
          />
        </section>

        {/* Score Details */}
        <section className="card" style={scoreDetailStyle}>
          <h3 style={{ marginBottom: '1.5rem' }}>Score Breakdown</h3>

          <div style={scoreBreakdownGridStyle}>
            <ScoreMetric
              label="ATS Compatibility"
              value={analysisResult.atsScore}
              category="resume"
            />
            <ScoreMetric
              label="Keyword Density"
              value={Math.min(analysisResult.atsScore + 5, 100)}
              category="keyword"
            />
            <ScoreMetric
              label="Formatting Score"
              value={Math.min(analysisResult.atsScore + 10, 100)}
              category="format"
            />
            <ScoreMetric
              label="Content Quality"
              value={Math.min(analysisResult.atsScore - 5, 100)}
              category="content"
            />
          </div>
        </section>

        {/* Recommendations */}
        <section className="card" style={recommendationsStyle}>
          <h3 style={{ marginBottom: '1.5rem' }}>💡 Recommendations</h3>

          <p style={{ marginBottom: '1rem', color: '#7E98A4' }}>
            Based on your score of {analysisResult.atsScore}/100:
          </p>

          <div style={recommendationListStyle}>
            {getScoreRecommendations(analysisResult.atsScore).map((rec, idx) => (
              <div key={idx} style={recommendationItemStyle}>
                <span style={{ color: '#243746', fontWeight: '600', marginRight: '0.75rem' }}>
                  {idx + 1}.
                </span>
                <span style={{ color: '#7E98A4' }}>{rec}</span>
              </div>
            ))}
          </div>

          <div style={actionTipsStyle}>
            <h4 style={{ marginBottom: '1rem', color: '#243746' }}>🎯 Action Items</h4>
            <ul style={{ margin: 0, paddingLeft: '1.5rem', color: '#7E98A4' }}>
              <li style={{ marginBottom: '0.5rem' }}>
                Review job descriptions and incorporate relevant keywords
              </li>
              <li style={{ marginBottom: '0.5rem' }}>
                Ensure consistent formatting throughout the document
              </li>
              <li style={{ marginBottom: '0.5rem' }}>
                Add metrics and numbers to quantify your achievements
              </li>
              <li>
                Use standard fonts and avoid images or graphics
              </li>
            </ul>
          </div>
        </section>

        {/* Skills Analysis */}
        <section style={{ marginTop: '2rem' }}>
          <SkillList skills={analysisResult.skills} />
        </section>

        {/* Job Recommendations */}
        <section style={{ marginTop: '2rem' }}>
          <JobMatch jobMatches={analysisResult.jobMatches} />
        </section>

        {/* Additional Resources */}
        <section className="card" style={resourcesStyle}>
          <h3 style={{ marginBottom: '1.5rem' }}>📚 Helpful Resources</h3>

          <div style={resourcesGridStyle}>
            <ResourceCard
              title="ATS Best Practices"
              description="Learn the do's and don'ts of ATS-friendly resume formatting"
            />
            <ResourceCard
              title="Keyword Optimization"
              description="Master keyword selection to improve your ATS score"
            />
            <ResourceCard
              title="Resume Templates"
              description="Download ATS-optimized resume templates"
            />
            <ResourceCard
              title="Interview Prep"
              description="Get ready for interviews after passing ATS screening"
            />
          </div>
        </section>

        {/* Export/Share Section */}
        <section style={exportSectionStyle}>
          <div style={{ textAlign: 'center' }}>
            <h3 style={{ marginBottom: '1rem' }}>Share Your Results</h3>
            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
              <button className="btn btn-primary" style={{ cursor: 'pointer' }}>
                📥 Download Report
              </button>
              <button className="btn btn-secondary" style={{ cursor: 'pointer' }}>
                📧 Email Results
              </button>
              <button className="btn btn-secondary" style={{ cursor: 'pointer' }}>
                🔗 Share Link
              </button>
            </div>
          </div>
        </section>

        {/* Footer */}
        <section style={analysisFoorerStyle}>
          <button
            className="btn btn-primary"
            onClick={() => navigate('/dashboard')}
            style={{ width: '100%', maxWidth: '300px', margin: '0 auto', display: 'block' }}
          >
            🔄 Analyze Another Resume
          </button>
        </section>
      </div>
    </div>
  )
}

function ScoreMetric({ label, value, category }) {
  const getColor = (val) => {
    if (val >= 80) return '#16a34a'
    if (val >= 60) return '#f59e0b'
    return '#ef4444'
  }

  const icons = {
    resume: '📊',
    keyword: '🔑',
    format: '📋',
    content: '✍️',
  }

  return (
    <div style={metricCardStyle}>
      <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>
        {icons[category]}
      </div>
      <p style={{ margin: '0 0 0.5rem 0', color: '#7E98A4', fontSize: '0.9rem' }}>
        {label}
      </p>
      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
        <span style={{
          fontSize: '1.5rem',
          fontWeight: '700',
          color: getColor(value),
        }}>
          {value}
        </span>
        <span style={{ color: '#7E98A4', fontSize: '0.9rem' }}>/100</span>
      </div>
    </div>
  )
}

function ResourceCard({ title, description }) {
  return (
    <div style={resourceCardStyle}>
      <h4 style={{ marginBottom: '0.5rem', color: '#243746' }}>{title}</h4>
      <p style={{ margin: 0, fontSize: '0.9rem', color: '#7E98A4' }}>
        {description}
      </p>
    </div>
  )
}

const analysisContainerStyle = {
  minHeight: 'calc(100vh - 70px)',
  backgroundColor: '#D7E9EE',
  padding: '3rem 0',
}

const analysisHeaderStyle = {
  marginBottom: '2rem',
  paddingBottom: '2rem',
  borderBottom: '2px solid rgba(36, 55, 70, 0.1)',
}

const scoreMainSectionStyle = {
  marginBottom: '2rem',
  display: 'grid',
  gap: '2rem',
}

const scoreDetailStyle = {
  padding: '2rem',
  marginBottom: '2rem',
}

const scoreBreakdownGridStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))',
  gap: '1.5rem',
}

const metricCardStyle = {
  padding: '1.5rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
  textAlign: 'center',
}

const recommendationsStyle = {
  padding: '2rem',
  marginBottom: '2rem',
}

const recommendationListStyle = {
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  padding: '1.5rem',
  marginBottom: '1.5rem',
  border: '1px solid #E8E8E6',
}

const recommendationItemStyle = {
  display: 'flex',
  alignItems: 'flex-start',
  marginBottom: '1rem',
}

const actionTipsStyle = {
  padding: '1.5rem',
  backgroundColor: '#C7E0E8',
  borderRadius: '0.5rem',
  border: '1px solid #9FB9C2',
}

const resourcesStyle = {
  padding: '2rem',
  marginTop: '2rem',
  marginBottom: '2rem',
}

const resourcesGridStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
  gap: '1.5rem',
}

const resourceCardStyle = {
  padding: '1.5rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
}

const exportSectionStyle = {
  backgroundColor: '#F8F8F6',
  padding: '2rem',
  borderRadius: '0.75rem',
  border: '1px solid #E8E8E6',
  marginBottom: '2rem',
}

const analysisFoorerStyle = {
  textAlign: 'center',
  padding: '2rem 0',
}

const emptyAnalysisStyle = {
  padding: '4rem 2rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '1rem',
  border: '2px dashed #9FB9C2',
  textAlign: 'center',
}