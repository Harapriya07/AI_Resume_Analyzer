import { useState } from 'react'
import { analyzeResume } from '../services/api'

export default function ResumeUpload({ onAnalysisComplete }) {
  const [isDragging, setIsDragging] = useState(false)
  const [file, setFile] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)

  const handleDrag = (e) => {
    e.preventDefault()
    setIsDragging(e.type === 'dragenter' || e.type === 'dragover')
  }

  const validateFile = (file) => {
    if (!file) {
      setError('No file selected')
      return false
    }

    const validTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'text/plain']
    if (!validTypes.includes(file.type)) {
      setError('Invalid file type. Please upload PDF, DOC, DOCX, or TXT')
      return false
    }

    if (file.size > 5 * 1024 * 1024) {
      setError('File size exceeds 5MB limit')
      return false
    }

    return true
  }

  const handleFile = async (uploadedFile) => {
    setError(null)
    setSuccess(null)

    if (!validateFile(uploadedFile)) {
      return
    }

    setFile(uploadedFile)
    setSuccess(`File "${uploadedFile.name}" selected`)
  }

  const handleDrop = (e) => {
    e.preventDefault()
    setIsDragging(false)
    const droppedFile = e.dataTransfer.files[0]
    handleFile(droppedFile)
  }

  const handleFileInput = (e) => {
    const selectedFile = e.target.files[0]
    handleFile(selectedFile)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!file) {
      setError('Please select a file first')
      return
    }

    setIsLoading(true)
    setError(null)

    try {
      const result = await analyzeResume(file)
      setSuccess('Resume analyzed successfully!')
      setFile(null)
      onAnalysisComplete(result)
    } catch (err) {
      setError('Failed to analyze resume. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="card" style={uploadContainerStyle}>
      <h3 style={{ marginBottom: '1.5rem', textAlign: 'center' }}>Upload Your Resume</h3>

      <form onSubmit={handleSubmit}>
        <div
          style={{
            ...dropZoneStyle,
            backgroundColor: isDragging ? '#C7E0E8' : '#F8F8F6',
            borderColor: isDragging ? '#243746' : '#9FB9C2',
          }}
          onDragEnter={handleDrag}
          onDragLeave={handleDrag}
          onDragOver={handleDrag}
          onDrop={handleDrop}
        >
          <div style={dropZoneContentStyle}>
            <span style={{ fontSize: '2.5rem', marginBottom: '0.5rem' }}>📤</span>
            <p style={{ fontWeight: '500', marginBottom: '0.5rem' }}>Drag and drop your resume here</p>
            <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>or click to select a file</p>
          </div>

          <input
            type="file"
            onChange={handleFileInput}
            accept=".pdf,.doc,.docx,.txt"
            style={{ display: 'none' }}
            id="resumeInput"
            disabled={isLoading}
          />

          <label
            htmlFor="resumeInput"
            style={{
              position: 'absolute',
              top: 0,
              left: 0,
              width: '100%',
              height: '100%',
              cursor: isLoading ? 'not-allowed' : 'pointer',
            }}
          />
        </div>

        {file && (
          <div style={fileInfoStyle}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <span style={{ fontSize: '1.2rem' }}>✓</span>
              <div>
                <p style={{ fontWeight: '500', margin: 0 }}>{file.name}</p>
                <p style={{ fontSize: '0.85rem', color: '#7E98A4', margin: 0 }}>
                  {(file.size / 1024).toFixed(2)} KB
                </p>
              </div>
            </div>
            <button
              type="button"
              onClick={() => {
                setFile(null)
                setSuccess(null)
              }}
              style={removeBtnStyle}
              disabled={isLoading}
            >
              Remove
            </button>
          </div>
        )}

        {error && <div className="error" style={{ marginTop: '1rem' }}>❌ {error}</div>}
        {success && <div className="success" style={{ marginTop: '1rem' }}>✓ {success}</div>}

        <button
          type="submit"
          className="btn btn-primary"
          style={{ width: '100%', marginTop: '1.5rem' }}
          disabled={!file || isLoading}
        >
          {isLoading ? (
            <>
              <span className="loading"></span>
              Analyzing...
            </>
          ) : (
            '🔍 Analyze Resume'
          )}
        </button>
      </form>

      <p style={disclaimerStyle}>
        Supports: PDF, DOC, DOCX, TXT | Max size: 5MB
      </p>
    </div>
  )
}

const uploadContainerStyle = {
  maxWidth: '500px',
  margin: '0 auto',
  padding: '2rem',
}

const dropZoneStyle = {
  border: '2px dashed #9FB9C2',
  borderRadius: '0.75rem',
  padding: '2rem',
  textAlign: 'center',
  position: 'relative',
  cursor: 'pointer',
  transition: 'all 0.3s ease',
  minHeight: '150px',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
}

const dropZoneContentStyle = {
  pointerEvents: 'none',
}

const fileInfoStyle = {
  backgroundColor: '#E8E8E6',
  border: '1px solid #9FB9C2',
  borderRadius: '0.5rem',
  padding: '1rem',
  marginTop: '1rem',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
}

const removeBtnStyle = {
  background: 'none',
  border: 'none',
  color: '#dc2626',
  fontWeight: '500',
  cursor: 'pointer',
  fontSize: '0.9rem',
  padding: '0.25rem 0.75rem',
}

const disclaimerStyle = {
  fontSize: '0.85rem',
  color: '#7E98A4',
  textAlign: 'center',
  marginTop: '1rem',
  margin: '1rem 0 0 0',
}