import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import ResumeUpload from '../components/ResumeUpload'
import ScoreCard from '../components/ScoreCard'
import SkillList from '../components/SkillList'
import JobMatch from '../components/JobMatch'

export default function Dashboard() {
  const [analysisResult, setAnalysisResult] = useState(null)
  const navigate = useNavigate()

  const handleAnalysisComplete = (result) => {
    setAnalysisResult(result)
    // Auto-scroll to results
    setTimeout(() => {
      const resultsSection = document.getElementById('results-section')
      if (resultsSection) {
        resultsSection.scrollIntoView({ behavior: 'smooth' })
      }
    }, 100)
  }

  const handleViewDetails = () => {
    navigate('/analysis', { state: { analysisResult } })
  }

  return (
    <div style={dashboardStyle}>
      <div className="container">
        {/* Header */}
        <section style={headerStyle}>
          <h1 style={{ marginBottom: '0.5rem' }}>Resume Analysis Dashboard</h1>
          <p style={{ color: '#7E98A4', fontSize: '1.05rem' }}>
            Upload your resume to get instant ATS scoring and optimization suggestions
          </p>
        </section>

        {/* Upload Section */}
        <section style={uploadSectionStyle}>
          <ResumeUpload onAnalysisComplete={handleAnalysisComplete} />
        </section>

        {/* Results Section */}
        {analysisResult && (
          <section id="results-section" style={resultsSectionStyle}>
            <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
              <h2>📊 Your Analysis Results</h2>
              <p style={{ color: '#7E98A4' }}>
                Based on your resume: <strong>{analysisResult.fileName}</strong>
              </p>
            </div>

            {/* ATS Score */}
            <div style={resultGridStyle}>
              <ScoreCard
                atsScore={analysisResult.atsScore}
                rating={analysisResult.rating}
                fileName={analysisResult.fileName}
                uploadedAt={analysisResult.uploadedAt}
              />
            </div>

            {/* Feedback Section */}
            <div className="card" style={feedbackCardStyle}>
              <h3 style={{ marginBottom: '1.5rem' }}>📝 Resume Feedback</h3>

              <div style={feedbackListStyle}>
                {analysisResult.feedback.map((item, index) => (
                  <div key={index} style={feedbackItemStyle}>
                    <span style={{ fontWeight: '500', color: '#243746' }}>
                      {item.startsWith('✓') ? (
                        <span style={{ color: '#16a34a' }}>✓</span>
                      ) : item.startsWith('⚠') ? (
                        <span style={{ color: '#f59e0b' }}>⚠</span>
                      ) : (
                        <span>•</span>
                      )}
                    </span>
                    <span style={{ flex: 1, marginLeft: '0.75rem' }}>
                      {item.replace(/^[✓⚠]/,'').trim()}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Skills Section */}
            <div style={{ marginTop: '2rem' }}>
              <SkillList skills={analysisResult.skills} />
            </div>

            {/* Job Matches */}
            <div style={{ marginTop: '2rem' }}>
              <JobMatch jobMatches={analysisResult.jobMatches} />
            </div>

            {/* Action Buttons */}
            <div style={actionButtonsStyle}>
              <button
                className="btn btn-primary"
                onClick={handleViewDetails}
                style={{ flex: 1, maxWidth: '300px' }}
              >
                📋 View Full Analysis
              </button>
              <button
                className="btn btn-secondary"
                onClick={() => setAnalysisResult(null)}
                style={{ flex: 1, maxWidth: '300px' }}
              >
                🔄 Analyze Another Resume
              </button>
            </div>
          </section>
        )}

        {/* Empty State */}
        {!analysisResult && (
          <section style={emptyStateStyle}>
            <div style={{ textAlign: 'center' }}>
              <p style={{ fontSize: '3rem', margin: '0 0 1rem 0' }}>📄</p>
              <h3 style={{ color: '#7E98A4', marginBottom: '0.5rem' }}>
                No analysis yet
              </h3>
              <p style={{ color: '#7E98A4' }}>
                Upload a resume to see detailed ATS analysis and recommendations
              </p>
            </div>
          </section>
        )}
      </div>
    </div>
  )
}

const dashboardStyle = {
  minHeight: 'calc(100vh - 70px)',
  backgroundColor: '#D7E9EE',
  padding: '3rem 0',
}

const headerStyle = {
  textAlign: 'center',
  marginBottom: '3rem',
  paddingBottom: '2rem',
  borderBottom: '2px solid rgba(36, 55, 70, 0.1)',
}

const uploadSectionStyle = {
  marginBottom: '3rem',
}

const resultsSectionStyle = {
  marginTop: '3rem',
  animation: 'fadeIn 0.5s ease',
}

const resultGridStyle = {
  display: 'grid',
  gap: '2rem',
  marginBottom: '2rem',
}

const feedbackCardStyle = {
  padding: '2rem',
}

const feedbackListStyle = {
  display: 'grid',
  gap: '1rem',
}

const feedbackItemStyle = {
  display: 'flex',
  alignItems: 'flex-start',
  padding: '1rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
}

const actionButtonsStyle = {
  display: 'flex',
  gap: '1rem',
  justifyContent: 'center',
  marginTop: '3rem',
  flexWrap: 'wrap',
  paddingBottom: '2rem',
}

const emptyStateStyle = {
  textAlign: 'center',
  padding: '3rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '1rem',
  border: '2px dashed #9FB9C2',
  marginTop: '2rem',
}